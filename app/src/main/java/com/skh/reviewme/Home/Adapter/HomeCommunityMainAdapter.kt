package com.skh.reviewme.Home.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Home.Model.HomeCommunityModel
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ItemCommunityHorizontalBinding


/**
 * Created by Seogki on 2018. 6. 5..
 */
open class HomeCommunityMainAdapter(context: Context, arrayList: MutableList<HomeCommunityModel>) : BaseRecyclerViewAdapter<HomeCommunityModel, HomeCommunityMainAdapter.ReviewMainViewholder>(context, arrayList) {


    override fun onBindView(holder: ReviewMainViewholder, position: Int) {
        holder.setIsRecyclable(false)
        val model = getItem(holder.adapterPosition)
        holder.binding.model = model
        if (model?.images != null)
            holder.binding.reviewMainTitle.visibility = View.GONE
    }

    override fun getItemId(position: Int): Long {
        val id = arrayList?.get(position)
        return id!!.communityid!!.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_community_horizontal, parent, false).let { ReviewMainViewholder(it) }

    inner class ReviewMainViewholder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemCommunityHorizontalBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
        }
    }

}