package com.skh.reviewme.Setting.Notification

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.R
import com.skh.reviewme.Setting.Model.SettingNotificationModel
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.ItemNotificationBinding

/**
 * Created by Seogki on 2018. 7. 24..
 */
open class SettingNotificationAdapter(context: Context, arrayList: MutableList<SettingNotificationModel>) : BaseRecyclerViewAdapter<SettingNotificationModel, SettingNotificationAdapter.SettingErrorViewHolder>(context, arrayList) {

    override fun onBindView(holder: SettingErrorViewHolder, position: Int) {
        holder.binding.model = getItem(holder.adapterPosition)
        holder.setIsRecyclable(true)
        DLog.e("model: ${getItem(position)}")

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false).let { SettingErrorViewHolder(it) }

    inner class SettingErrorViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemNotificationBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
        }
    }
}






