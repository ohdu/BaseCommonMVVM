package cn.ondu.chat.bean

data class FromTypeListBean(
    var actors: String?,
    var count_series: Int?,
    var country: Any?,
    var cover: String?,
    var created_at: Any?,
    var `data`: List<Data>?,
    var data_source: Any?,
    var hits: Any?,
    var id: String,
    var introduction: String?,
    var lang: Any?,
    var miner: Any?,
    var name: String?,
    var producer: Any?,
    var rank: Int?,
    var region: String?,
    var score: Any?,
    var source: String?,
    var source_key: String?,
    var style: Any?,
    var subname: Any?,
    var tags: Any?,
    var type: Any?,
    var type_name: Any?,
    var updated_at: Any?,
    var year: String?
)

data class Data(
    var name: String,
    var url: String
)

