package com.skh.reviewme.Community

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Community.model.CommunityModel
import com.skh.reviewme.R
import com.skh.reviewme.databinding.FragmentCommunityMainBinding


class CommunityMainFragment : BaseFragment() {

    lateinit var binding: FragmentCommunityMainBinding
    private lateinit var communityMainAdapter: CommunityMainAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_main, container, false)

        setView()
        return binding.root
    }

    private fun setView() {

        val list = ArrayList<CommunityModel>()

        val model1 = CommunityModel("skh","닌텐도 게임 사고싶다")
        val model2 = CommunityModel("skh","흠ㅎ므")
        val model3 = CommunityModel("skh","ㅇㄴㅇㅁ")
        val model4 = CommunityModel("skh","ㄲㄲ")
        val model5 = CommunityModel("skh","ㅃㅃㅃㅃㅃ")
        val model6 = CommunityModel("skh","ㅁㅁㅁㅁㅁㅁㅁㅁ")
        val model7 = CommunityModel("skh","ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ")

        val model8 = CommunityModel("skh","닌텐도 게임 사고싶다")
        val model9 = CommunityModel("skh","흠ㅎ므")
        val model10 = CommunityModel("skh","ㅇㄴㅇㅁ")
        val model11 = CommunityModel("skh","ㄲㄲ")
        val model12 = CommunityModel("skh","ㅃㅃㅃㅃㅃ")
        val model13 = CommunityModel("skh","ㅁㅁㅁㅁㅁㅁㅁㅁ")
        val model14 = CommunityModel("skh","ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ")

        list.add(model1)
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
        list.add(model13)
        list.add(model14)


        communityMainAdapter = CommunityMainAdapter(context!!, list)
        layoutManager = LinearLayoutManager(context!!,  LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        (layoutManager as LinearLayoutManager).initialPrefetchItemCount = 5
        binding.mainGridRv.layoutManager = layoutManager
        binding.mainGridRv.adapter = communityMainAdapter
        binding.mainGridRv.setItemViewCacheSize(20)
        binding.mainGridRv.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        binding.mainGridRv.setHasFixedSize(true)

        val decor = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decor.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.survey_divder)!!)
        binding.mainGridRv.addItemDecoration(decor)
    }


}// Required empty public constructor
