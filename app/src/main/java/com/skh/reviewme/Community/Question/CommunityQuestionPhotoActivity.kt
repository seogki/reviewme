package com.skh.reviewme.Community.Question

import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Main.Interface.HashMapListener
import com.skh.reviewme.R
import com.skh.reviewme.Util.ImageFile
import com.skh.reviewme.databinding.ActivityCommunityQuestionPhotoBinding

class CommunityQuestionPhotoActivity : AppCompatActivity(), HashMapListener, View.OnClickListener, BaseRecyclerViewAdapter.OnItemClickListener {



    lateinit var binding: ActivityCommunityQuestionPhotoBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var communityQuestionPhotoAdapter: CommunityQuestionPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_community_question_photo)



        setRv()
    }

    private fun setRv() {
        binding.questionPhotoRv.setHasFixedSize(true)
        layoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        binding.questionPhotoRv.layoutManager = layoutManager
        communityQuestionPhotoAdapter = CommunityQuestionPhotoAdapter(this, ImageFile().fetchAllImages(this))
        binding.questionPhotoRv.adapter = communityQuestionPhotoAdapter
        communityQuestionPhotoAdapter.sethash(this)
        binding.onClickListener = this
        communityQuestionPhotoAdapter.setOnItemClickListener(this)
    }

    override fun onHash(pos: Int, filename: String?) {
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.question_btn_register -> {

            }
        }

    }
    override fun onItemClick(view: View, position: Int) {
        for (i in 0..(view as ViewGroup).childCount) {
            val child = view.getChildAt(i)

            if(child is ImageView){
                checkImageIsAvailable(child.drawable)
            }
        }
    }


    private fun checkImageIsAvailable(drawable: Drawable) {
        val bitmap1 = binding.questionPhotoImg1.drawable
        val bitmap2 = binding.questionPhotoImg2.drawable
        val bitmap3 = binding.questionPhotoImg3.drawable
        val bitmap4 = binding.questionPhotoImg4.drawable

        if(bitmap1 == drawable){
            binding.questionPhotoImg1.setImageDrawable(null)
            return
        }
        if(bitmap2 == drawable){
            binding.questionPhotoImg2.setImageDrawable(null)
            return
        }
        if(bitmap3 == drawable){
            binding.questionPhotoImg3.setImageDrawable(null)
            return
        }
        if(bitmap4 == drawable){
            binding.questionPhotoImg4.setImageDrawable(null)
            return
        }

        if(bitmap1 == null){
            binding.questionPhotoImg1.setImageDrawable(drawable)
        } else {
            if(bitmap2 == null){
                binding.questionPhotoImg2.setImageDrawable(drawable)
            } else{
                if(bitmap3 == null){
                    binding.questionPhotoImg3.setImageDrawable(drawable)
                } else {
                    if(bitmap4 == null){
                        binding.questionPhotoImg4.setImageDrawable(drawable)
                    }
                }
            }
        }


    }
}
