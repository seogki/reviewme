package com.skh.reviewme.Main

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Main.model.ReviewFragmentModel
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ItemReviewMainBinding

/**
 * Created by Seogki on 2018. 6. 5..
 */
open class ReviewMainAdapter(context: Context, arrayList: MutableList<ReviewFragmentModel>) : BaseRecyclerViewAdapter<ReviewFragmentModel, ReviewMainAdapter.viewholder>(context, arrayList) {

    override fun onBindView(holder: viewholder, position: Int) {
//        holder.binding.item = getItem(position)
        holder.binding.model = getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_review_main, parent, false).let { viewholder(it) }

    inner class viewholder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val binding: ItemReviewMainBinding = DataBindingUtil.bind(itemView)

        init {
            binding.onClickListener = this
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.main_review_img -> {
//                    if (getItem(adapterPosition)!!.images.contains("jpg") || getItem(adapterPosition)!!.images?.contains("png")) {
//
//                        DLog.e("getItem : " + getItem(position = adapterPosition)!!.images)
//                        val intent = Intent(context, GalleryMaxActivity::class.java)
//                        intent.putExtra("file", "file://" + getItem(position = adapterPosition)!!.images)
//                        context?.startActivity(intent)
//                    }
                }
            }
        }


    }
}