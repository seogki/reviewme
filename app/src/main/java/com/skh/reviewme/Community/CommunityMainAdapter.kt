package com.skh.reviewme.Community

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
import com.skh.reviewme.Community.model.CommunityModel
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ItemCommunityMainBinding
import java.util.*

/**
 * Created by Seogki on 2018. 4. 12..
 */

class CommunityMainAdapter(context: Context, commu: ArrayList<CommunityModel>) : BaseRecyclerViewAdapter<CommunityModel, CommunityMainAdapter.CommunityViewHolder>(context, commu) {


    private var commu: ArrayList<CommunityModel>? = commu
    private var mcontext: Context? = context


    override fun onBindView(holder: CommunityViewHolder, position: Int) {
        holder.binding.item = getItem(holder.adapterPosition)
        holder.setIsRecyclable(false)
        holder.binding.imgThumbnail.setImageURI(null)
        Glide.with(context!!).clear(holder.binding.imgThumbnail)
        setImage(holder.binding.imgThumbnail, getItem(holder.adapterPosition)?.image)
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
                            .circleCrop()
                            .override(100, 100)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_community_main, parent, false)
        return CommunityViewHolder(view)
    }


    inner class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val binding: ItemCommunityMainBinding = DataBindingUtil.bind(itemView)

        init {

        }

        override fun onClick(v: View) {

            when (v.id) {


            }

        }


    }


}
