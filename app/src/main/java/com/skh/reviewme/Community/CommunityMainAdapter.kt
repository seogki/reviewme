package com.skh.reviewme.Community

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        holder.setIsRecyclable(true)
        if (holder.binding.item.image.isNullOrEmpty()) {
            holder.binding.imgThumbnail.visibility = View.GONE
        } else {
            holder.binding.imgThumbnail.visibility = View.VISIBLE
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
