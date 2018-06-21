package com.skh.reviewme.Main

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Community.CommunityMainActivity
import com.skh.reviewme.R
import com.skh.reviewme.Setting.SettingMainActivity
import com.skh.reviewme.databinding.ActivityReviewMainBinding

class ReviewMainActivity : BaseActivity(), View.OnClickListener {


    lateinit var binding: ActivityReviewMainBinding
    private var backKeyPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_main)
        binding.layoutBottomTab.onClickListener = this
        addFragment(R.id.frame_layout, ReviewMainFragment(), false, false)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn2 -> {
                beginActivity(Intent(this@ReviewMainActivity
                        , CommunityMainActivity::class.java)
                        )
            }
            R.id.bottom_layout_btn3 -> {
                beginActivity(Intent(this@ReviewMainActivity
                        , SettingMainActivity::class.java)
                        )
            }

        }
    }


    override fun onBackPressed() {


        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            this.finishAffinity()
            finishToast()
        }

    }
}
