package com.skh.reviewme.Community

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Main.ReviewMainActivity
import com.skh.reviewme.R
import com.skh.reviewme.Setting.SettingMainActivity
import com.skh.reviewme.databinding.ActivityCommunityMainBinding

class CommunityMainActivity : BaseActivity(), View.OnClickListener {


    lateinit var binding: ActivityCommunityMainBinding
    private var backKeyPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_main)
        binding.layoutBottomTab.onClickListener = this

        addFragment(R.id.frame_layout, CommunityMainFragment(), false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn1 -> {
                beginActivity(Intent(this@CommunityMainActivity, ReviewMainActivity::class.java))
            }


            R.id.bottom_layout_btn3 -> {
                beginActivity(Intent(this@CommunityMainActivity, SettingMainActivity::class.java))
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
