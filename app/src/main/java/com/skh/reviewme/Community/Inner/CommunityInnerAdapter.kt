package com.skh.reviewme.Community.Inner

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Community.model.CommunityInnerModel
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ItemCommunityInnerBinding
import java.util.*

/**
 * Created by Seogki on 2018. 4. 12..
 */

class CommunityInnerAdapter(context: Context, commu: ArrayList<CommunityInnerModel>) : BaseRecyclerViewAdapter<CommunityInnerModel, CommunityInnerAdapter.CommunityInnerViewHolder>(context, commu) {


    private var commu: ArrayList<CommunityInnerModel>? = commu
    private var mcontext: Context? = context


    override fun onBindView(holder: CommunityInnerViewHolder, position: Int) {
        holder.binding.item = getItem(position)
        holder.setIsRecyclable(false)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_community_inner, parent, false)
        return CommunityInnerViewHolder(view)
    }


    inner class CommunityInnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val binding: ItemCommunityInnerBinding = DataBindingUtil.bind(itemView)
        init {

        }

        override fun onClick(v: View) {

            when (v.id) {


            }

        }


    }


}
