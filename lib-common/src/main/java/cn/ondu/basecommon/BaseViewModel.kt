package cn.ondu.basecommon

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cn.ondu.basecommon.http.ExceptionHandle
import cn.ondu.basecommon.http.HttpException
import cn.ondu.basecommon.http.IBaseBean
import cn.ondu.basecommon.http.HttpStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {
    /**
     * 一些比较复杂处理,比如多个接口的并发请求 则通过外部实现block()方法来完成
     */
    protected fun <T> httpComplex(
        resultLiveData: MutableLiveData<HttpStatus<T>>,
        block: suspend FlowCollector<T>.() -> Unit
    ) {

        viewModelScope.launch(Dispatchers.Main) {
            flow<T> {
                block()
            }
                .flowOn(Dispatchers.IO)
                .onStart {
                    resultLiveData.value = HttpStatus.LoadingStatus()
                }
                .catch { error ->
                    val handleException = ExceptionHandle.handleException(error)
                    resultLiveData.value = HttpStatus.ErrorStatus(handleException.errorMsg)
                }
                .onCompletion {
                    resultLiveData.value = HttpStatus.FinishStatus()
                }
                .collect {
                    resultLiveData.value = HttpStatus.SuccessStatus(it)
                }
        }

    }

    protected fun <T> httpToLiveData(
        block: suspend () -> T
    ) = liveData<HttpStatus<T>>(Dispatchers.Main) {
        emit(HttpStatus.LoadingStatus())
        try {
            //repository里已经将异常抛出 这里直接捕获就行
            emit(HttpStatus.SuccessStatus(block()))
        } catch (error: Exception) {
            error.printStackTrace()
            val handleException = ExceptionHandle.handleException(error)
            emit(HttpStatus.ErrorStatus(handleException.errorMsg))
        } finally {
            emit(HttpStatus.FinishStatus())
        }
    }

    protected fun <T> http(
        resultLiveData: MutableLiveData<HttpStatus<T>>,
        block: suspend () -> T
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            flow {
                val httpResult = block()
                    emit(httpResult)
            }
                .flowOn(Dispatchers.IO)
                .onStart {
                    resultLiveData.value = HttpStatus.LoadingStatus()
                }
                .catch { error ->
                    val handleException = ExceptionHandle.handleException(error)
                    resultLiveData.value = HttpStatus.ErrorStatus(handleException.errorMsg)
                }
                .onCompletion {
                    resultLiveData.value = HttpStatus.FinishStatus()
                }
                .collect {
                    resultLiveData.value = HttpStatus.SuccessStatus(it)
                }
        }

    }
}