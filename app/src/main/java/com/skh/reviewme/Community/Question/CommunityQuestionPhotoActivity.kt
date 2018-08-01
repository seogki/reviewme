package com.skh.reviewme.Community.Question

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.R
import com.skh.reviewme.Review.Interface.HashMapListener
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.ImageFile
import com.skh.reviewme.databinding.ActivityCommunityQuestionPhotoBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CommunityQuestionPhotoActivity : BaseActivity(), HashMapListener, View.OnClickListener, BaseRecyclerViewAdapter.OnItemClickListener {


    lateinit var binding: ActivityCommunityQuestionPhotoBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var communityQuestionPhotoAdapter: CommunityQuestionPhotoAdapter
    private lateinit var imagePath: ArrayList<String>
    private lateinit var imagepath1: String
    private lateinit var imagepath2: String
    private lateinit var imagepath3: String
    private lateinit var imagepath4: String
    var mCurrentPhotoPath: String? = null
    private var requestFileCode: Int = 4798

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_question_photo)
        binding.onClickListener = this
        imagePath = ArrayList<String>()
        binding.questionPhotoImgBack.drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        binding.questionBtnFloataction.drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        setRv()
    }

    private fun setRv() {
        binding.questionPhotoRv.setHasFixedSize(true)
        layoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        binding.questionPhotoRv.layoutManager = layoutManager
        communityQuestionPhotoAdapter = CommunityQuestionPhotoAdapter(this, ImageFile().fetchAllImages(this))

        communityQuestionPhotoAdapter.sethash(this)
        communityQuestionPhotoAdapter.setOnItemClickListener(this)
        communityQuestionPhotoAdapter.setHasStableIds(true)


        Handler().postDelayed({
            binding.questionPhotoRv.adapter = communityQuestionPhotoAdapter
        }, 100)
    }

    override fun onHash(pos: Int, filename: String?) {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.question_btn_register -> {
                callActivity()
            }
            R.id.question_photo_img_back -> {
                finish()
            }
            R.id.question_btn_floataction -> {
                startCamera()
            }
            R.id.question_photo_img1_close -> {

                binding.questionPhotoImg1.setImageDrawable(null)
                binding.questionPhotoImg1Close.visibility = View.GONE
                imagePath.remove(imagepath1)
            }
            R.id.question_photo_img2_close -> {

                binding.questionPhotoImg2.setImageDrawable(null)
                binding.questionPhotoImg2Close.visibility = View.GONE
                imagePath.remove(imagepath2)
            }
            R.id.question_photo_img3_close -> {

                binding.questionPhotoImg3.setImageDrawable(null)
                binding.questionPhotoImg3Close.visibility = View.GONE
                imagePath.remove(imagepath3)
            }
            R.id.question_photo_img4_close -> {
                binding.questionPhotoImg4.setImageDrawable(null)
                binding.questionPhotoImg4Close.visibility = View.GONE
                imagePath.remove(imagepath4)

            }
        }

    }

    override fun onItemClick(view: View, position: Int) {
        checkImageIsAvailable(communityQuestionPhotoAdapter.getItem(position).toString())
    }


    private fun callActivity() {
        val returnIntent = Intent()
        returnIntent.putStringArrayListExtra("imagePath", imagePath)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun checkImageIsAvailable(drawable: String) {
        val bitmap1 = binding.questionPhotoImg1.drawable
        val bitmap2 = binding.questionPhotoImg2.drawable
        val bitmap3 = binding.questionPhotoImg3.drawable
        val bitmap4 = binding.questionPhotoImg4.drawable


        val uri = Uri.parse("file://$drawable")

        if (bitmap1 == null) {
            imagepath1 = drawable
            glideDrawable(uri, binding.questionPhotoImg1)
            binding.questionPhotoImg1Close.visibility = View.VISIBLE
            imagePath.add(drawable)
        } else {
            if (bitmap2 == null) {
                imagepath2 = drawable
                glideDrawable(uri, binding.questionPhotoImg2)
                binding.questionPhotoImg2Close.visibility = View.VISIBLE
                imagePath.add(drawable)
            } else {
                if (bitmap3 == null) {
                    imagepath3 = drawable
                    glideDrawable(uri, binding.questionPhotoImg3)
                    binding.questionPhotoImg3Close.visibility = View.VISIBLE
                    imagePath.add(drawable)
                } else {
                    if (bitmap4 == null) {
                        imagepath4 = drawable
                        glideDrawable(uri, binding.questionPhotoImg4)
                        binding.questionPhotoImg4Close.visibility = View.VISIBLE
                        imagePath.add(drawable)
                    }
                }
            }
        }


    }

    private fun glideDrawable(uri: Uri, imageView: ImageView) {
        DLog.e("uri + $uri")
        Glide.with(this).load(uri)
                .apply(RequestOptions()
                        .centerCrop()
                        .override(200, 200)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .thumbnail(0.1f)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        DLog.e("setImage")
                        imageView.setImageDrawable(resource)
                    }

                })
    }

    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                DLog.e("created camera")
            } catch (e: Exception) {
                // Error occurred while creating the File
                e.printStackTrace()
            }
            // Continue only if the File was successfully created

            if (photoFile != null) {
                if (Build.VERSION_CODES.N <= android.os.Build.VERSION.SDK_INT) {
                    val photoURI = FileProvider.getUriForFile(this@CommunityQuestionPhotoActivity
                            , "com.example.android.fileprovider"
                            , photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    DLog.e("call FileProvider")
                    startActivityForResult(takePictureIntent, requestFileCode)
                } else {
                    val uri = Uri.fromFile(photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(takePictureIntent, requestFileCode)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestFileCode && resultCode == RESULT_OK) {
            DLog.e("activity done : $mCurrentPhotoPath")
            if (mCurrentPhotoPath != null) {
                galleryAddPic()
                checkImageIsAvailable(mCurrentPhotoPath!!)

            }
        }
    }


    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }
}
