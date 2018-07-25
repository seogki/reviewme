package com.skh.reviewme.Main


import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Main.Photos.ReviewPhotoActivity
import com.skh.reviewme.Main.model.ReviewFragmentModel
import com.skh.reviewme.Network.ApiCilentRx
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.GridSpacingItemDecoration
import com.skh.reviewme.Util.UtilMethod
import com.skh.reviewme.databinding.FragmentReviewMainBinding
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
open class ReviewMainFragment : BaseFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener {


    private lateinit var binding: FragmentReviewMainBinding
    private lateinit var reviewAdapter: ReviewMainAdapter
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var name: String
    private lateinit var dialog: android.app.AlertDialog
    private var isOpened: Boolean = false
    private var isLoading: Boolean = false
    private var pref: SharedPreferences? = null
    private var isSearch: Boolean = false
    private var searchedText: String? = null

    private val client by lazy { ApiCilentRx.create() }
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_main, container, false)

        pref = activity?.getSharedPreferences("UserId", Activity.MODE_PRIVATE)
        binding.onClickListener = this
        binding.reviewMainQuestion.onClickListener = this
        binding.mainSearchEdit.setOnEditorActionListener(this)
        binding.appBarLayout.isEnabled = false // Include 한 레이아웃이 상위로 가기 위해 사용
        setView()

        return binding.root
    }


    private fun setSpotDialogs() {
        dialog = SpotsDialog
                .Builder()
                .setContext(context!!)
                .setMessage("데이터를 불러오는 중...")
                .setCancelable(false)
                .build().apply { show() }
    }


    private fun setView() {
        binding.reviewConstAll.post {
            run { plusClose(true) }
        }

        binding.mainGridRv.removeAllViews()
        reviewAdapter = ReviewMainAdapter(context!!, ArrayList<ReviewFragmentModel>())
        layoutManager = GridLayoutManager(context!!, 2, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 3
        binding.mainGridRv.layoutManager = layoutManager
        binding.mainGridRv.setHasFixedSize(true)
        binding.mainGridRv.isDrawingCacheEnabled = true
        binding.mainGridRv.setItemViewCacheSize(20)
        binding.mainGridRv.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        reviewAdapter.setHasStableIds(true)
        binding.mainGridRv.itemAnimator = null
        binding.mainGridRv.addItemDecoration(GridSpacingItemDecoration(2, 15, false, 0))

        binding.swipeLayout.setDistanceToTriggerSync(350)
        binding.swipeLayout.setOnRefreshListener(this)

        Handler().postDelayed({
            binding.mainGridRv.adapter = reviewAdapter
        }, 100)

        setSpotDialogs()
        getApi()


    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {

        when (actionId) {
            EditorInfo.IME_ACTION_SEARCH -> {
                callSearchApi()
            }
        }
        return true
    }


    private fun getApi() {

        disposable = client.GetReviewItem2Rx().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe({ result ->
                    DLog.e("memory : " + UtilMethod.getMemoryUsage(reviewAdapter.itemCount))
                    reviewAdapter.addItems(result?.reviewModel as MutableList<ReviewFragmentModel>)
                    dialog.dismiss()
                    startScrollbarListener()
                }, { error ->
                    DLog.e("error ${error?.message.toString()}")
                    dialog.dismiss()
                })
    }

    private fun startScrollbarListener() {
        Handler().postDelayed({
            setRecyclerViewScrollbar()
        }, 100)
    }

    private fun setRecyclerViewScrollbar() {
        binding.mainGridRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView!!.canScrollVertically(1)) {
                    if (!dialog.isShowing && !isLoading) {
                        if (!isSearch) {
                            isLoading = true
                            dialog.show()
                            scrollToEnd()
                        } else {
                            isLoading = true
                            dialog.show()
                            callScrollSearchedApi()
                        }
                    }
                }
            }
        })
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
            R.id.main_search_txt -> {
                callSearchApi()
            }
        }
    }

    private fun callSearchApi() {
        searchedText = binding.mainSearchEdit?.text?.toString() ?: return
        if (searchedText!!.isNotEmpty()) {

            val userid = pref?.getString("userLoginId", "") ?: return
            onRefreshWithoutApi()
            disposable = client.GetSearchedReviewItem2Rx(userid, searchedText!!).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        DLog.e("memory : " + UtilMethod.getMemoryUsage(reviewAdapter.itemCount))
                        clearAndClose(binding.mainSearchEdit)
                        reviewAdapter.addItems(result?.reviewModel as MutableList<ReviewFragmentModel>)
                        dialog.dismiss()
                        startScrollbarListener()
                    }, { error ->
                        DLog.e("error ${error?.message.toString()}")
                        dialog.dismiss()
                    })


        } else {
            Toast.makeText(context, "모두 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }


    private fun callScrollSearchedApi() {

        if (reviewAdapter.itemCount < 1) {
            dialog.dismiss()
            isLoading = false
        } else {
            val userid = pref?.getString("userLoginId", "") ?: return
            disposable = reviewAdapter.getItem(reviewAdapter.itemCount - 1)?.reviewId?.let {
                client.GetScrollSearchedReviewItem2Rx(userid, searchedText!!, it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            isLoading = if (result?.reviewModel?.isEmpty() == true) {
                                dialog.dismiss()
                                false
                            } else {
                                reviewAdapter.addItems(result?.reviewModel as MutableList<ReviewFragmentModel>)
                                DLog.e("memory : " + UtilMethod.getMemoryUsage(reviewAdapter.itemCount))
                                dialog.dismiss()
                                false
                            }
                        }, { error ->
                            DLog.e("error ${error?.message.toString()}")
                            isLoading = false
                            dialog.dismiss()
                        })
            }
        }
    }

    private fun addReview() {
        if (name != "empty") {
            when {
                binding.reviewMainQuestion.naviTextTitle.text.toString().isEmpty() -> {
                    Toast.makeText(context, "타이틀을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                    return
                }
                binding.reviewMainQuestion.naviTxtQuestion.text.toString().isEmpty() -> {
                    Toast.makeText(context, "리뷰를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                    return
                }
                else -> {
                    sendReviewToServer()
                }
            }
        } else {
            Toast.makeText(context, "이미지를 정해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun sendReviewToServer() {

        val userid = pref?.getString("userLoginId", "").let { RequestBody.create(MediaType.parse("text/plain"), it) }

        val file = UtilMethod.getCompressed(context!!, File(name).toString(), "drawable1")

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file).let { MultipartBody.Part.createFormData("images", file.name, it) }


        val title = binding.reviewMainQuestion.naviTextTitle.text.toString().trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }
        val text = binding.reviewMainQuestion.naviTxtQuestion.text.toString().trim().let { RequestBody.create(MediaType.parse("text/plain"), it) }

        disposable = client.SetReviewPhotosRx(userid, title, text, requestFile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe({
                    plusClose(false)
                    setNavigationNull()
                    closeKeyboard()
                    onRefresh()
                }, { error ->
                    closeKeyboard()
                    DLog.e("이미지 업로드 fail")
                    DLog.e("t : " + error?.message)
                })
    }

    private fun scrollToEnd() {

        if (reviewAdapter.itemCount < 1) {
            dialog.dismiss()
            isLoading = false
        } else {
            disposable = reviewAdapter.getItem(reviewAdapter.itemCount - 1)?.reviewId?.let {
                client.ScrollGetReviewItem2Rx(it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            isLoading = if (result?.reviewModel?.isEmpty() == true) {
                                dialog.dismiss();false
                            } else {
                                reviewAdapter.addItems(result?.reviewModel as MutableList<ReviewFragmentModel>)
                                DLog.e("memory : " + UtilMethod.getMemoryUsage(reviewAdapter.itemCount))
                                dialog.dismiss()
                                false
                            }
                        }, { error ->
                            DLog.e("error ${error?.message.toString()}")
                            isLoading = false
                            dialog.dismiss()
                        })
            }
        }
    }


    override fun onResume() {
        super.onResume()
        val pref = context?.getSharedPreferences("fileName", MODE_PRIVATE)
        this.name = pref?.getString("filestring", "empty")!!
        if (name != "empty") {
            Uri.parse("file://$name").let {
                Glide.with(this@ReviewMainFragment)
                        .load(it)
                        .apply(RequestOptions().centerCrop().override(400, 400))
                        .into(object : SimpleTarget<Drawable>() {
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                binding.reviewMainQuestion.naviImg.setImageDrawable(resource)
                            }

                        })
            }
        }
        pref.edit().remove("fileName").apply()
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

    override fun onRefresh() {
        reviewAdapter.clearItems()
        reviewAdapter.notifyDataSetChanged()
        binding.mainGridRv.removeOnScrollListener(null)
        getApi()
        isSearch = false
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }

    private fun onRefreshWithoutApi() {
        reviewAdapter.clearItems()
        reviewAdapter.notifyDataSetChanged()
        isSearch = true
        binding.mainGridRv.removeOnScrollListener(null)
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
                        binding.reviewMainRegi.text = "-"
                        DLog.e("isOpend: $isOpened")
                    }
                })
    }

    private fun setNavigationNull() {
        binding.reviewMainQuestion.naviImg.setImageDrawable(null)
        binding.reviewMainQuestion.naviTextTitle.text = null
        binding.reviewMainQuestion.naviTxtQuestion.text = null
    }

    private fun clearAndClose(edit: EditText) {
        edit.text.clear()
        closeKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(context!!).clearMemory()
        disposable?.dispose()
    }

}// Required empty public constructor
