package com.skh.reviewme.Review

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.skh.reviewme.Base.BaseActivity
import com.skh.reviewme.Community.CommunityMainActivity
import com.skh.reviewme.Home.HomeMainActivity
import com.skh.reviewme.R
import com.skh.reviewme.Setting.SettingMainActivity
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.ActivityReviewMainBinding

class ReviewMainActivity : BaseActivity(), View.OnClickListener {


    lateinit var binding: ActivityReviewMainBinding
    private var backKeyPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_main)
        binding.layoutBottomTab.onClickListener = this
        addFragment(R.id.frame_layout, ReviewMainFragment(), false, false, "ReviewMainFragment")
        setCurrentTab()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn0 -> {
                beginActivity(Intent(this@ReviewMainActivity
                        , HomeMainActivity::class.java)
                )
            }

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

    private fun setCurrentTab() {
        binding.layoutBottomTab.bottomLayoutBtn1Txt.setImageDrawable(ContextCompat.getDrawable(this@ReviewMainActivity, R.drawable.icons8_home_24_fill))
        binding.layoutBottomTab.bottomLayoutBtn1Txt.drawable.setColorFilter(Color.parseColor("#13A9AA"), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab.bottomLayoutText1.setTextColor(Color.parseColor("#13A9AA"))
    }


    override fun onBackPressed() {
        DLog.e("onBack Pressed" + isFirstFragment())

        if (isFirstFragment()) {
            val frag = supportFragmentManager.findFragmentById(R.id.frame_layout) as ReviewMainFragment
            DLog.e("frag : " + frag.tag)
            if (frag.isOpened) {
                frag.plusClose(false)
            } else {
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
        } else {
            super.onBackPressed()
        }

    }

    private fun isFirstFragment(): Boolean {
        val curFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        DLog.e("current Fragment ${curFragment.tag}")
        return curFragment.tag == "ReviewMainFragment"
    }
}
