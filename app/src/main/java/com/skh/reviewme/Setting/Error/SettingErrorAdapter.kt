package com.skh.reviewme.Setting.Error

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.R
import com.skh.reviewme.Setting.Model.SettingErrorModel
import com.skh.reviewme.databinding.ItemSettingErrorBinding

/**
 * Created by Seogki on 2018. 7. 24..
 */
open class SettingErrorAdapter(context: Context, arrayList: MutableList<SettingErrorModel>) : BaseRecyclerViewAdapter<SettingErrorModel, SettingErrorAdapter.SettingErrorViewHolder>(context, arrayList) {

    override fun onBindView(holder: SettingErrorViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        holder.binding.model = getItem(holder.adapterPosition)
    }

    override fun getItemId(position: Int): Long {
        val id = arrayList?.get(position)
        return id!!.ErrorContent!!.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_setting_error, parent, false).let { SettingErrorViewHolder(it) }

    inner class SettingErrorViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemSettingErrorBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
        }
    }
}






