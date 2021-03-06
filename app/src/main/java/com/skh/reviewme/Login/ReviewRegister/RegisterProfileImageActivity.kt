package com.skh.reviewme.Login.ReviewRegister

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Review.Interface.HashMapListener
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.ImageFile
import com.skh.reviewme.databinding.ActivityRegisterProfileImageBinding

class RegisterProfileImageActivity : BaseActivity(), HashMapListener, View.OnClickListener {


    lateinit var binding: ActivityRegisterProfileImageBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var galleryAdapter: RegisterImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@RegisterProfileImageActivity, R.layout.activity_register_profile_image)
        binding.registerPhotoImgBack.drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        binding.btnFloataction.drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        setView()

    }

    private fun setView() {
        binding.registerPhotoRv.setHasFixedSize(true)
        layoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        binding.registerPhotoRv.layoutManager = layoutManager
        galleryAdapter = RegisterImageAdapter(this, ImageFile().fetchAllImages(this))

        galleryAdapter.sethash(this)
        binding.onClickListener = this

        Handler().postDelayed({
            binding.registerPhotoRv.adapter = galleryAdapter
        }, 100)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register_photo_img_back -> {
                finish()
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    override fun onHash(pos: Int, filename: String) {
        DLog.e("int : String : $pos : $filename")
        val pref = getSharedPreferences("fileName", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("filestring", filename)
        editor.commit()
        finish()
    }
}
