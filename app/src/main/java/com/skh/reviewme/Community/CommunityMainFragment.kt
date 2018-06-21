package com.skh.reviewme.Community

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Community.InnerActivity.CommunityInnerFragment
import com.skh.reviewme.Community.InnerActivity.CommunityQuestionActivity
import com.skh.reviewme.Community.model.CommunityModel
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.databinding.FragmentCommunityMainBinding


class CommunityMainFragment : BaseFragment(), View.OnClickListener, BaseRecyclerViewAdapter.OnItemClickListener {


    lateinit var binding: FragmentCommunityMainBinding
    private lateinit var communityMainAdapter: CommunityMainAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_main, container, false)
        binding.onClickListener = this

        setView()
        return binding.root
    }

    private fun setView() {

        communityMainAdapter = CommunityMainAdapter(context!!, addlist())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        (layoutManager as LinearLayoutManager).initialPrefetchItemCount = 5
        binding.mainGridRv.layoutManager = layoutManager
        binding.mainGridRv.adapter = communityMainAdapter
        binding.mainGridRv.setItemViewCacheSize(20)
        binding.mainGridRv.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_AUTO
        binding.mainGridRv.setHasFixedSize(false)

        val decor = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        decor.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.survey_divder)!!)
        binding.mainGridRv.addItemDecoration(decor)

        communityMainAdapter.setOnItemClickListener(this)
    }


    override fun onItemClick(view: View, position: Int) {
        DLog.e("item $position")

        var Images: ImageView? = null
        var text: TextView? = null
        var title: TextView? = null

        for (i in 0..(view as ViewGroup).childCount) {
            val child = view.getChildAt(i)
            title = (view.getChildAt(0) as TextView)
            text = view.getChildAt(1) as TextView


            if (child is TextView) {
                DLog.e("child + ${child.text}")

            } else if (child is ImageView) {
                DLog.e("child + image ${child.drawable}")
                Images = child
            }
        }


        if (Images != null) {

            val frag = CommunityInnerFragment()
            val bundle = Bundle()
            bundle.putString("title", title?.text.toString())
            bundle.putString("text", text?.text.toString())
            bundle.putParcelable("IMAGE", (Images.drawable as BitmapDrawable).bitmap)
            frag.arguments = bundle
            addFragment(activity, R.id.frame_layout, frag, false, true)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.community_btn_cat -> {
                beginNewActivity(Intent(context, CommunityQuestionActivity::class.java))
            }
        }


    }

    private fun addlist(): ArrayList<CommunityModel> {
        val list = ArrayList<CommunityModel>()

        val model1 = CommunityModel("skh", "닌텐도 게임 사고싶다", getURLForResource(R.drawable.test1))
        val model2 = CommunityModel("skh", "흠ㅎ므", "")
        val model3 = CommunityModel("skh", "ㅇㄴㅇㅁ", "")
        val model4 = CommunityModel("skh", "ㄲㄲ", getURLForResource(R.drawable.test3))
        val model5 = CommunityModel("skh", "ㅃㅃㅃㅃㅃ", "")
        val model6 = CommunityModel("skh", "ㅁㅁㅁㅁㅁㅁㅁㅁ", getURLForResource(R.drawable.test6))
        val model7 = CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", "")

        val model8 = CommunityModel("skh", "닌텐도 게임 사고싶다", "")
        val model9 = CommunityModel("skh", "흠ㅎ므", getURLForResource(R.drawable.test2))
        val model10 = CommunityModel("skh", "ㅇㄴㅇㅁ", "")
        val model11 = CommunityModel("skh", "ㄲㄲ", getURLForResource(R.drawable.test5))
        val model12 = CommunityModel("skh", "ㅃㅃㅃㅃㅃ", "")
        val model13 = CommunityModel("skh", "ㅁㅁㅁㅁㅁㅁㅁㅁ", getURLForResource(R.drawable.test4))
        val model14 = CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", "")

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

        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))

        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))
        list.add(CommunityModel("skh", "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", ""))

        return list;
    }

}// Required empty public constructor
