package com.skh.reviewme.Main

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Main.model.ReviewFragmentModel
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ItemReviewMainBinding

/**
 * Created by Seogki on 2018. 6. 5..
 */
open class ReviewMainAdapter(context: Context, arrayList: MutableList<ReviewFragmentModel>) : BaseRecyclerViewAdapter<ReviewFragmentModel, ReviewMainAdapter.ReviewMainViewholder>(context, arrayList) {

    override fun onBindView(holder: ReviewMainViewholder, position: Int) {
        holder.setIsRecyclable(false)
        holder.binding.model = getItem(holder.adapterPosition)
        holder.binding.mainReviewImg.setImageURI(null)
        Glide.with(context!!).clear(holder.binding.mainReviewImg)
        setImage(holder.binding.mainReviewImg, getItem(holder.adapterPosition)?.images)

    }

    private fun setImage(imageView: ImageView, images: String?) {

        if (images == null) {
            Glide.with(context!!).clear(imageView)
            imageView.setImageDrawable(null)
        } else {
            val decodedString = Base64.decode(images, Base64.DEFAULT)
            Glide.with(imageView.context)
                    .load(decodedString)
                    .apply(RequestOptions()
                            .centerCrop()
                            .override(190, 190)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .thumbnail(0.1f)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            imageView.setImageDrawable(resource)
                        }
                    })

        }
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