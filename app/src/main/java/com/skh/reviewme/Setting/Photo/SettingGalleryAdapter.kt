package com.skh.reviewme.Setting.Photo

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Main.Interface.HashMapListener
import com.skh.reviewme.Main.Photos.GalleryMaxActivity
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ItemPhotosBinding
import java.util.*

/**
 * Created by Seogki on 2018. 4. 12..
 */

class SettingGalleryAdapter(context: Context, images: ArrayList<String>) : BaseRecyclerViewAdapter<String, SettingGalleryAdapter.GalleryViewHolder>(context, images) {


    private var images: ArrayList<String>? = images
    private var mcontext: Context? = context
    private lateinit var hashMapListener: HashMapListener


    override fun onBindView(holder: GalleryViewHolder, position: Int) {

        holder.binding.item = getItem(position)
        holder.setIsRecyclable(true)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_photos, parent, false)
        return GalleryViewHolder(view)
    }

    fun sethash(hashMapListener: HashMapListener) {
        this.hashMapListener = hashMapListener
    }


    inner class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        val binding: ItemPhotosBinding = DataBindingUtil.bind(itemView)

        init {
            binding.onClickListener = this
        }

        override fun onClick(v: View) {

            when (v.id) {

                R.id.photo_img -> {
                    hashMapListener.onHash(adapterPosition, returnString())
                }
                R.id.photo_max_btn -> {

                    val intent = Intent(context, GalleryMaxActivity::class.java)
                    intent.putExtra("file", returnImage())
                    context?.startActivity(intent)
                }
            }

        }

        private fun returnImage(): String {
            return "file://" + getItem(adapterPosition)
        }

        private fun returnString(): String {
            return "" + getItem(adapterPosition)
        }

    }


}
