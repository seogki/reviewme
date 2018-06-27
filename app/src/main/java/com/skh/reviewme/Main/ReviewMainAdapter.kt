package com.skh.reviewme.Main

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.widget.RecyclerView
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
open class ReviewMainAdapter(context: Context, arrayList: MutableList<ReviewFragmentModel>) : BaseRecyclerViewAdapter<ReviewFragmentModel, ReviewMainAdapter.viewholder>(context, arrayList) {

    override fun onBindView(holder: viewholder, position: Int) {
//        holder.binding.item = getItem(position)
        holder.binding.model = getItem(position)
        setimage(holder.binding.reviewMainIdImg)
    }

    private fun setimage(imageView: ImageView) {


        val uri2 = Uri.parse("android.resource://" + R::class.java.`package`.name + "/" + R.drawable.test_10).toString()

        Glide.with(imageView.context)
                .load(uri2)
                .apply(RequestOptions()
                        .centerCrop()
                        .circleCrop()
                        .override(50, 50)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .thumbnail(0.1f)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        imageView.setImageDrawable(resource)
                    }
                })
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

                }
            }
        }


    }
}