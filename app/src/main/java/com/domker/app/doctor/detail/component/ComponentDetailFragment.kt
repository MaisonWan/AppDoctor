package com.domker.app.doctor.detail.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentDetailComponentInfoBinding
import com.domker.app.doctor.detail.AppDetailListAdapter
import com.domker.base.addDividerItemDecoration

/**
 * 组件详细信息
 *
 * Created by wanlipeng on 2020/6/10 5:43 PM
 */
class ComponentDetailFragment : Fragment() {
    // ViewModel
    private lateinit var componentDetailViewModel: ComponentDetailViewModel
    private lateinit var mListAdapter: AppDetailListAdapter
    private lateinit var binding: FragmentDetailComponentInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        componentDetailViewModel = ViewModelProvider(this)[ComponentDetailViewModel::class.java]
        binding = FragmentDetailComponentInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val p = arguments?.get("component") as ComponentInfo
        initView(p)
        componentDetailViewModel.updateData(requireContext(), p)
        initObserver()
    }

    private fun initView(c: ComponentInfo) {
        val context = requireContext()
        binding.activityName.text = c.shortName
        binding.recyclerViewActivityInfo.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewActivityInfo.addDividerItemDecoration(context, R.drawable.inset_recyclerview_divider)
        binding.recyclerViewActivityInfo.setItemViewCacheSize(100)
        mListAdapter = AppDetailListAdapter(context)
        binding.recyclerViewActivityInfo.adapter = mListAdapter
    }

    /**
     * 监听初始化监听器
     */
    private fun initObserver() {
        componentDetailViewModel.getDetail().observe(viewLifecycleOwner) { detail ->
            binding.appIcon.setImageDrawable(detail.icon)
            binding.appPackage.text = detail.name

            mListAdapter.setDetailList(detail.itemList)
            mListAdapter.notifyDataSetChanged()
        }
    }

}