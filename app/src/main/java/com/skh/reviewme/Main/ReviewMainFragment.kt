package com.skh.reviewme.Main


import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Main.Photos.ReviewPhotoActivity
import com.skh.reviewme.Main.model.ReviewModel
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.FragmentReviewMainBinding


/**
 * A simple [Fragment] subclass.
 */
open class ReviewMainFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentReviewMainBinding
    private lateinit var reviewAdpater: ReviewMainAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var name: String
    private var isOpened: Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_main, container, false)


        binding.onClickListener = this
        binding.reviewMainQuestion.onClickListener = this

        setView()

        return binding.root
    }


    private fun setView() {
//        var list: ArrayList<String> = ArrayList()
//
//        for (i in 1..16) {
//            list.add(i.toString())
//        }
        val list: ArrayList<ReviewModel> = ArrayList()

//        val model = ReviewModel("이렇게 잘뽑힘",getURLForResource(R.drawable.test1),"돼지 코팩 쑥쑥 뽑혀 나옴")
//
//
//        val model2 = ReviewModel("정말 딱맞음",getURLForResource(R.drawable.test2),"맞춤구두 하는이유")
//
//        val model3 = ReviewModel("내 최 메뉴 탄생",getURLForResource(R.drawable.test3),"감자랑 케첩은 진리")
//        val model4 = ReviewModel("완전 잘되는 뽑기샵",getURLForResource(R.drawable.test4),"ㄲㄲㄲㄲ")
//        val model5 = ReviewModel("2호선 실화임",getURLForResource(R.drawable.test5),"안가")
//        val model6 = ReviewModel("대박적",getURLForResource(R.drawable.test6),"인생템 말 필요 없음")

        val model = ReviewModel("1",getURLForResource(R.drawable.test1),"1")


        val model2 = ReviewModel("2",getURLForResource(R.drawable.test2),"2")

        val model3 = ReviewModel("3",getURLForResource(R.drawable.test3),"3")
        val model4 = ReviewModel("4",getURLForResource(R.drawable.test4),"4")
        val model5 = ReviewModel("5",getURLForResource(R.drawable.test5),"5")
        val model6 = ReviewModel("6",getURLForResource(R.drawable.test6),"6")

        val model7 = ReviewModel("1",getURLForResource(R.drawable.test1),"1")


        val model8 = ReviewModel("2",getURLForResource(R.drawable.test2),"2")

        val model9 = ReviewModel("3",getURLForResource(R.drawable.test3),"3")
        val model10 = ReviewModel("4",getURLForResource(R.drawable.test4),"4")
        val model11 = ReviewModel("5",getURLForResource(R.drawable.test5),"5")
        val model12 = ReviewModel("6",getURLForResource(R.drawable.test6),"6")

        list.add(model)
        list.add(model2)
        list.add(model3)
        list.add(model4)
        list.add(model5)
        list.add(model6)
        list.add(model7)
        list.add(model8)
        list.add(model9)
        list.add(model10)
        list.add(model11)
        list.add(model12)



        reviewAdpater = ReviewMainAdapter(context!!, list)
        layoutManager = GridLayoutManager(context!!, 2, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        (layoutManager as GridLayoutManager).initialPrefetchItemCount = 5
        binding.mainGridRv.layoutManager = layoutManager
        binding.mainGridRv.adapter = reviewAdpater
        binding.mainGridRv.setItemViewCacheSize(20)
        binding.mainGridRv.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        binding.mainGridRv.setHasFixedSize(false)

        val decorVertical = ContextCompat.getDrawable(context!!, R.drawable.survey_divder)
        val decorHorizontal = ContextCompat.getDrawable(context!!, R.drawable.survey_divder_horizontal)

        binding.mainGridRv.addItemDecoration(GridDividerItemDecoration(decorHorizontal, decorVertical, 2))

        binding.reviewConstAll.post {
            run { plusClose(true) }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.review_main_regi ->
                if (isOpened) {
                    plusClose(false)

                } else {
                    plusOpen()
                }
            R.id.navi_img -> {
                setPhoto()
            }
            R.id.navi_btn_register -> {
                addReview()
            }
        }
    }

    private fun addReview() {
        if (name != "empty") {
            reviewAdpater.addItem(ReviewModel(binding.reviewMainQuestion.naviTextTitle.text.toString(),name,binding.reviewMainQuestion.naviTxtQuestion.text.toString()))
            plusClose(false)
            binding.reviewMainQuestion.naviImg.setImageDrawable(null)
            name = "empty"
        } else {

        }
    }


    private fun setPhoto() {
        TedPermission.with(context)
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        Handler().postDelayed({
                            beginNewActivity(Intent(context, ReviewPhotoActivity::class.java))
                        }, 10)
                    }

                    override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>) {

                    }
                }).check()

    }

    override fun onResume() {
        super.onResume()
        val pref = context?.getSharedPreferences("fileName", MODE_PRIVATE)
        this.name = pref?.getString("filestring", "empty")!!
        if (name != "empty") {
            Uri.parse("file://$name")
                    .let {
                        Glide.with(this@ReviewMainFragment)
                                .load(it)
                                .apply(RequestOptions().centerCrop().override(500, 500))
                                .into(object : SimpleTarget<Drawable>() {
                                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                        binding.reviewMainQuestion.naviImg.setImageDrawable(resource)
                                    }

                                })
                    }
        }
    }

    private fun plusClose(isFirst: Boolean) {
        val view: View = binding.reviewConstAll
        if (isFirst)
            view.visibility = View.INVISIBLE
        view.animate().translationXBy((view.width * 0.86).toFloat())
                .setDuration(300)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        isOpened = false
                        binding.reviewMainQuestion.naviItems.isClickable = false
                        binding.reviewMainRegi.text = "+"


                        if (isFirst)
                            view.visibility = View.VISIBLE
                    }
                })
    }

    private fun plusOpen() {
        val view: View = binding.reviewConstAll
        view.animate().translationXBy(-(view.width * 0.86).toFloat())
                .setDuration(300)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        isOpened = true
                        binding.reviewMainQuestion.naviItems.isClickable = true
                        binding.reviewMainQuestion.naviItems.requestFocus()
                        binding.reviewMainRegi.text = "-"
                        DLog.e("isOpend: $isOpened")
                    }
                })
    }

}// Required empty public constructor
