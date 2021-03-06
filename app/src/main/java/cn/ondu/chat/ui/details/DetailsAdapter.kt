package cn.ondu.chat.ui.details

import cn.ondu.basecommon.base.BaseBindingQuickAdapter
import cn.ondu.basecommon.util.easyColor
import cn.ondu.chat.R
import cn.ondu.chat.bean.Data
import cn.ondu.chat.databinding.ItemCountSeriesBinding

class DetailsAdapter :
    BaseBindingQuickAdapter<Data, ItemCountSeriesBinding>(ItemCountSeriesBinding::inflate) {

    /**
     * 当前观看级数
     */
    var watchIndex = 0
        set(value) {
            if (field == value) {
                return
            }
            field = value
            notifyDataSetChanged()
        }

    override fun convert(holder: BaseBindingHolder, item: Data) {
        holder.viewBinding<ItemCountSeriesBinding>().apply {
            tvText.text = item.name
            if (holder.adapterPosition == watchIndex) {
                tvText.setTextColor(context.easyColor(R.color.colorPrimary))
            } else {
                tvText.setTextColor(context.easyColor(R.color.colorUncheck))
            }
        }
    }
}