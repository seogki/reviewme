package com.skh.reviewme.Home


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Network.ApiCilentRx
import com.skh.reviewme.R
import com.skh.reviewme.Review.model.ReviewFragmentModel
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.UtilMethod
import com.skh.reviewme.databinding.FragmentHomeMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * A simple [Fragment] subclass.
 */
class HomeMainFragment : Fragment() {

    lateinit var binding: FragmentHomeMainBinding
    private lateinit var reviewAdapter: HomeReviewMainAdapter
    private lateinit var layoutManager: GridLayoutManager
    private val client by lazy { ApiCilentRx.create() }
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main, container, false)
        setReview()
        return binding.root
    }


    private fun setReview() {
        reviewAdapter = HomeReviewMainAdapter(context!!, ArrayList<ReviewFragmentModel>())
        layoutManager = GridLayoutManager(context!!,1, GridLayoutManager.HORIZONTAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 3
        binding.homeRvReview.layoutManager = layoutManager
        binding.homeRvReview.setHasFixedSize(true)
        binding.homeRvReview.isDrawingCacheEnabled = true
        binding.homeRvReview.setItemViewCacheSize(20)
        binding.homeRvReview.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        reviewAdapter.setHasStableIds(true)
        binding.homeRvReview.itemAnimator = null
//        binding.homeRvReview.addItemDecoration(GridSpacingItemDecoration(1, 35, false, 0))

        Handler().postDelayed({
            binding.homeRvReview.adapter = reviewAdapter
        }, 100)


        getApi()
    }

    private fun getApi() {

        disposable = client.GetReviewItem2Rx().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe({ result ->
                    DLog.e("memory : " + UtilMethod.getMemoryUsage(reviewAdapter.itemCount))
                    reviewAdapter.addItems(result?.reviewModel as MutableList<ReviewFragmentModel>)

                }, { error ->
                    DLog.e("error ${error?.message.toString()}")
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}// Required empty public constructor
