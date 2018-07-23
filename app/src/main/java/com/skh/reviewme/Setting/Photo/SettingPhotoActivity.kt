package com.skh.reviewme.Setting.Photo

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.gson.JsonObject
import com.skh.reviewme.Main.Interface.HashMapListener
import com.skh.reviewme.Network.ApiCilent
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.ImageFile
import com.skh.reviewme.Util.UtilMethod
import com.skh.reviewme.databinding.ActivityReviewPhotoBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SettingPhotoActivity : AppCompatActivity(), View.OnClickListener, HashMapListener {

    private lateinit var binding: ActivityReviewPhotoBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var galleryAdapter: SettingGalleryAdapter
    private var requestFileCode: Int = 4798
    private lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_photo)
        pref = getSharedPreferences("UserId", Activity.MODE_PRIVATE)
        binding.onClickListener = this
        setView()
    }

    private fun setView() {

        layoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        binding.reviewPhotoRv.layoutManager = layoutManager
        galleryAdapter = SettingGalleryAdapter(this, ImageFile().fetchAllImages(this))
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

    override fun onHash(pos: Int, filename: String) {
        DLog.e("int : String : $pos : $filename")
        binding.reviewPhotoRv.isEnabled = false
        changeProfile(filename)

    }

    private fun changeProfile(name: String) {
        val file = UtilMethod.getCompressed(this@SettingPhotoActivity, File(name).toString(), "drawable1")
        val userid = pref.getString("userLoginId", "").trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file).let { MultipartBody.Part.createFormData("images", file.name, it) }
        val call = ApiCilent.getInstance().getService().SetSettingProfileImage(userid, requestFile)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                DLog.e(t?.message.toString())
                AlertDialog.Builder(this@SettingPhotoActivity, R.style.MyDialogTheme)
                        .setMessage("오류 발생")
                        .setPositiveButton("확인", { dialog, _ ->
                            dialog.dismiss()
                            binding.reviewPhotoRv.isEnabled = true
                        }).setNegativeButton(null, null)
                        .show()
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {

                AlertDialog.Builder(this@SettingPhotoActivity, R.style.MyDialogTheme)
                        .setMessage("프로필 사진이 변경 되었습니다")
                        .setPositiveButton("확인", { dialog, _ ->
                            dialog.dismiss()
                            finish()
                            binding.reviewPhotoRv.isEnabled = true
                        }).setNegativeButton(null, null)
                        .show()
            }

        })
    }
}
