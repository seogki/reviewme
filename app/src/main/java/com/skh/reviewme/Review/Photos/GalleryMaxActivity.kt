package com.skh.reviewme.Review.Photos

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ActivityGalleryMaxBinding

class GalleryMaxActivity : AppCompatActivity() {

    lateinit var binding: ActivityGalleryMaxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_max)
        val intent = intent
        val fileName = intent.getStringExtra("file")
        Uri.parse(fileName).let { Glide.with(this).load(it).apply(RequestOptions.noTransformation()).into(binding.photoMaxImg) }
    }
}
