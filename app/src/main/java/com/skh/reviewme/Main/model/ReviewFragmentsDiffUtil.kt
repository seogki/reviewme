package com.skh.reviewme.Main.model

import android.support.v7.util.DiffUtil

/**
 * Created by Seogki on 2018. 7. 19..
 */
open class ReviewFragmentsDiffUtil(oldList: MutableList<ReviewFragmentModel>?, newList: List<ReviewFragmentModels>) : DiffUtil.Callback() {

    private val oldList: List<ReviewFragmentModels>? = null
    private val newList: List<ReviewFragmentModels>? = null

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList!![oldItemPosition].reviewModel!! == newList!![newItemPosition].reviewModel
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList!![oldItemPosition] == newList!![newItemPosition];

    }

    override fun getOldListSize(): Int = oldList!!.size

    override fun getNewListSize(): Int = newList!!.size


}