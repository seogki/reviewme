package com.skh.reviewme.Community.Question

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
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
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.ImageFile
import com.skh.reviewme.databinding.ActivityCommunityQuestionPhotoBinding

class CommunityQuestionPhotoActivity : AppCompatActivity(), HashMapListener, View.OnClickListener, BaseRecyclerViewAdapter.OnItemClickListener {


    lateinit var binding: ActivityCommunityQuestionPhotoBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var communityQuestionPhotoAdapter: CommunityQuestionPhotoAdapter
    private lateinit var imagePath: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_question_photo)
        binding.onClickListener = this
        imagePath = ArrayList<String>()

        setRv()
    }

    private fun setRv() {
        binding.questionPhotoRv.setHasFixedSize(true)
        layoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        binding.questionPhotoRv.layoutManager = layoutManager
        communityQuestionPhotoAdapter = CommunityQuestionPhotoAdapter(this, ImageFile().fetchAllImages(this))

        communityQuestionPhotoAdapter.sethash(this)
        communityQuestionPhotoAdapter.setHasStableIds(true)
        communityQuestionPhotoAdapter.setOnItemClickListener(this)

        Handler().postDelayed({
            binding.questionPhotoRv.adapter = communityQuestionPhotoAdapter
        }, 100)
    }

    override fun onHash(pos: Int, filename: String?) {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.question_btn_register -> {
                val returnIntent = Intent()
                returnIntent.putParcelableArrayListExtra("PhotoFromQuestion", getDrawable())
                returnIntent.putStringArrayListExtra("imagePath",imagePath)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }

    }

    override fun onItemClick(view: View, position: Int) {

        DLog.e("data + " + communityQuestionPhotoAdapter.getItem(position).toString())
        imagePath.add(communityQuestionPhotoAdapter.getItem(position).toString())

        for (i in 0..(view as ViewGroup).childCount) {
            val child = view.getChildAt(i)

            if (child is ImageView) {
                checkImageIsAvailable(child.drawable)
            }
        }
    }

    private fun getDrawable(): ArrayList<Bitmap> {
        val drawable1 = (binding.questionPhotoImg1.drawable as? BitmapDrawable)?.bitmap
        val drawable2 = (binding.questionPhotoImg2.drawable as? BitmapDrawable)?.bitmap
        val drawable3 = (binding.questionPhotoImg3.drawable as? BitmapDrawable)?.bitmap
        val drawable4 = (binding.questionPhotoImg4.drawable as? BitmapDrawable)?.bitmap

        val list = ArrayList<Bitmap>()

        if (drawable1 != null) {
            list.add(drawable1)
        }
        if (drawable2 != null) {
            list.add(drawable2)
        }
        if (drawable3 != null) {
            list.add(drawable3)
        }
        if (drawable4 != null) {
            list.add(drawable4)
        }

        return list

    }

    private fun checkImageIsAvailable(drawable: Drawable) {
        val bitmap1 = binding.questionPhotoImg1.drawable
        val bitmap2 = binding.questionPhotoImg2.drawable
        val bitmap3 = binding.questionPhotoImg3.drawable
        val bitmap4 = binding.questionPhotoImg4.drawable

        if (bitmap1 == drawable) {
            binding.questionPhotoImg1.setImageDrawable(null)
            return
        }
        if (bitmap2 == drawable) {
            binding.questionPhotoImg2.setImageDrawable(null)
            return
        }
        if (bitmap3 == drawable) {
            binding.questionPhotoImg3.setImageDrawable(null)
            return
        }
        if (bitmap4 == drawable) {
            binding.questionPhotoImg4.setImageDrawable(null)
            return
        }

        if (bitmap1 == null) {
            binding.questionPhotoImg1.setImageDrawable(drawable)
        } else {
            if (bitmap2 == null) {
                binding.questionPhotoImg2.setImageDrawable(drawable)
            } else {
                if (bitmap3 == null) {
                    binding.questionPhotoImg3.setImageDrawable(drawable)
                } else {
                    if (bitmap4 == null) {
                        binding.questionPhotoImg4.setImageDrawable(drawable)
                    }
                }
            }
        }


    }
}
