package com.skh.reviewme.Community

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Home.HomeMainActivity
import com.skh.reviewme.R
import com.skh.reviewme.Review.ReviewMainActivity
import com.skh.reviewme.Setting.SettingMainActivity
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.ActivityCommunityMainBinding

class CommunityMainActivity : BaseActivity(), View.OnClickListener {


    lateinit var binding: ActivityCommunityMainBinding
    private var backKeyPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_main)
        binding.layoutBottomTab.onClickListener = this
        addFragment(R.id.frame_layout, CommunityMainFragment(), false, false, "CommunityMainFragment")
        setCurrentTab()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn0 -> {
                beginActivity(Intent(this@CommunityMainActivity
                        , HomeMainActivity::class.java)
                )
            }

            R.id.bottom_layout_btn1 -> {
                beginActivity(Intent(this@CommunityMainActivity, ReviewMainActivity::class.java))
            }


            R.id.bottom_layout_btn3 -> {
                beginActivity(Intent(this@CommunityMainActivity, SettingMainActivity::class.java))
            }

        }
    }

    private fun setCurrentTab() {
        binding.layoutBottomTab.bottomLayoutBtn2Txt.setImageDrawable(ContextCompat.getDrawable(this@CommunityMainActivity, R.drawable.icons8_people_24_fill))
        binding.layoutBottomTab.bottomLayoutBtn2Txt.drawable.setColorFilter(ContextCompat.getColor(this,R.color.titleColor), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab.bottomLayoutText2.setTextColor(ContextCompat.getColor(this,R.color.titleColor))
    }

    override fun onBackPressed() {
        DLog.e("onBack Pressed" + isFirstFragment())

        if (isFirstFragment()) {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis()
                showGuide()
                return
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                this.finishAffinity()
                finishToast()
            }
        } else {
            super.onBackPressed()
        }

    }

    private fun isFirstFragment(): Boolean {
        val curFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        DLog.e("current Fragment ${curFragment.tag}")
        return curFragment.tag == "CommunityMainFragment"
    }


}
