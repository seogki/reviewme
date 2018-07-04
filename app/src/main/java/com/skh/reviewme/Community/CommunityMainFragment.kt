package com.skh.reviewme.Community

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.skh.reviewme.Base.BaseFragment
import com.skh.reviewme.Base.BaseRecyclerViewAdapter
import com.skh.reviewme.Community.Inner.CommunityInnerFragment
import com.skh.reviewme.Community.Question.CommunityQuestionActivity
import com.skh.reviewme.Community.model.CommunityModel
import com.skh.reviewme.R
import com.skh.reviewme.Util.DLog
import com.skh.reviewme.Util.GridSpacingItemDecoration
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

        binding.mainGridRv.addItemDecoration(GridSpacingItemDecoration(1, 20, false, 0))

        communityMainAdapter.setOnItemClickListener(this)
    }


    override fun onItemClick(view: View, position: Int) {
        DLog.e("item $position")

        var Images: ImageView? = null
        var text: TextView? = null
        var title: TextView? = null

        for (i in 0..(view as ViewGroup).childCount) {
//            val child = view.getChildAt(i)
            title = (view.getChildAt(0) as TextView)
            text = view.getChildAt(1) as TextView
            Images = view.getChildAt(2) as ImageView
        }

        val frag = CommunityInnerFragment()
        frag.arguments = setBundle(title?.text.toString(), text?.text.toString(), (Images?.drawable as? BitmapDrawable)?.bitmap)
        addFragment(activity, R.id.frame_layout, frag, false, true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.community_btn_cat -> {
                beginNewActivity(Intent(context, CommunityQuestionActivity::class.java))
            }
        }
    }

    private fun setBundle(title: String, text: String, Images: Bitmap?): Bundle {
        val bundle = Bundle()

        if (Images != null) {
            bundle.putString("title", title)
            bundle.putString("text", text)
            bundle.putParcelable("IMAGE", Images)
        } else {
            bundle.putString("title", title)
            bundle.putString("text", text)
        }

        return bundle
    }

    private fun addlist(): ArrayList<CommunityModel> {
        val list = ArrayList<CommunityModel>()

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

        return list
    }

}// Required empty public constructor
