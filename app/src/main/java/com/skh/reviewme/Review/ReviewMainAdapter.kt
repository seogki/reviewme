package com.skh.reviewme.Review

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Review.model.ReviewFragmentModel
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ItemReviewMainBinding


/**
 * Created by Seogki on 2018. 6. 5..
 */
open class ReviewMainAdapter(context: Context, arrayList: MutableList<ReviewFragmentModel>) : BaseRecyclerViewAdapter<ReviewFragmentModel, ReviewMainAdapter.ReviewMainViewholder>(context, arrayList) {


    override fun onBindView(holder: ReviewMainViewholder, position: Int) {
        holder.setIsRecyclable(true)
        holder.binding.model = getItem(holder.adapterPosition)

    }

    override fun getItemId(position: Int): Long {
        val id = arrayList?.get(position)
        return id!!.reviewId!!.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_review_main, parent, false).let { ReviewMainViewholder(it) }

    inner class ReviewMainViewholder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemReviewMainBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
        }
    }
}