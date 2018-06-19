package com.skh.reviewme.Community.InnerActivity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.skh.reviewme.R
import com.skh.reviewme.databinding.ActivityCommunityQuestionBinding

class CommunityQuestionActivity : AppCompatActivity() {

    lateinit var binding: ActivityCommunityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_community_question)

    }
}
