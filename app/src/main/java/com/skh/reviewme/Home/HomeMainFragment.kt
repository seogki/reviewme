package com.skh.reviewme.Home


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Home.Adapter.HomeCommunityMainAdapter
import com.skh.reviewme.Home.Adapter.HomeReviewMainAdapter
import com.skh.reviewme.Home.Model.HomeCommunityModel
import com.skh.reviewme.Home.Model.HomeReviewFragmentModel
import com.skh.reviewme.Network.ApiCilentRx
import com.skh.reviewme.R
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
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var layoutManager2: LinearLayoutManager
    private lateinit var communityMainAdapter: HomeCommunityMainAdapter
    private val client by lazy { ApiCilentRx.create() }
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main, container, false)
        setRecyclerview()
        return binding.root
    }


    private fun setRecyclerview() {
        setReview()
        setCommunity()
        Handler().postDelayed({
            binding.homeRvReview.adapter = reviewAdapter
            binding.homeRvCommunity.adapter = communityMainAdapter
        }, 10)


        getApi()
        getComapi()
    }

    private fun setCommunity() {
        communityMainAdapter = HomeCommunityMainAdapter(context!!, ArrayList<HomeCommunityModel>())
        communityMainAdapter.setHasStableIds(true)
        layoutManager2 = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        layoutManager2.isItemPrefetchEnabled = true
        layoutManager2.initialPrefetchItemCount = 3
        binding.homeRvCommunity.isNestedScrollingEnabled = false
        binding.homeRvCommunity.layoutManager = layoutManager2
        binding.homeRvCommunity.itemAnimator = null

    }


    private fun setReview() {
        reviewAdapter = HomeReviewMainAdapter(context!!, ArrayList<HomeReviewFragmentModel>())
        reviewAdapter.setHasStableIds(true)
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 3
        binding.homeRvReview.isNestedScrollingEnabled = false
        binding.homeRvReview.layoutManager = layoutManager
        binding.homeRvReview.itemAnimator = null
    }

    private fun getApi() {

        disposable = client.GetHomeReviewItem2Rx().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe({ result ->
                    DLog.e("memory : " + UtilMethod.getMemoryUsage(reviewAdapter.itemCount))
                    reviewAdapter.addItems(result?.reviewModel as MutableList<HomeReviewFragmentModel>)

                }, { error ->
                    DLog.e("error ${error?.message.toString()}")
                })
    }

    private fun getComapi() {
        disposable = client.GetHomeCommunityItem2Rx().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe({ result ->
                    DLog.e("memory : " + UtilMethod.getMemoryUsage(communityMainAdapter.itemCount))
                    communityMainAdapter.addItems(result?.CommunityModel as MutableList<HomeCommunityModel>)

                }, { error ->
                    DLog.e("error ${error?.message.toString()}")
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}// Required empty public constructor
