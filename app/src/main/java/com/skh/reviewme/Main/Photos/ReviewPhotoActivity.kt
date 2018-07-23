package com.skh.reviewme.Main.Photos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.skh.reviewme.Main.Interface.HashMapListener
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.ImageFile
import com.skh.reviewme.databinding.ActivityReviewPhotoBinding


class ReviewPhotoActivity : AppCompatActivity(), View.OnClickListener, HashMapListener {

    private lateinit var binding: ActivityReviewPhotoBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var galleryAdapter: GalleryAdapter
    private var requestFileCode: Int = 4798
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_photo)
        binding.onClickListener = this
        setView()
    }

    private fun setView() {

        layoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        binding.reviewPhotoRv.layoutManager = layoutManager
        galleryAdapter = GalleryAdapter(this, ImageFile().fetchAllImages(this))
        galleryAdapter.sethash(this)
        binding.reviewPhotoRv.setHasFixedSize(false)

        Handler().postDelayed({
            binding.reviewPhotoRv.adapter = galleryAdapter
        }, 100)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_floataction -> {
                startCamera()
            }
        }
    }


    private fun startCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null)
            startActivityForResult(cameraIntent, requestFileCode)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val extras = data?.extras
            DLog.e("data: " + extras?.get("data"))
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
