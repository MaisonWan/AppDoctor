package com.domker.app.doctor.detail.component

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentDetailComponentInfoBinding
import com.domker.app.doctor.detail.AppDetailListAdapter
import com.domker.app.doctor.util.IntentUtil
import com.domker.base.addDividerItemDecoration
import com.domker.base.toastLong
import com.google.android.material.snackbar.Snackbar

/**
 * 组件详细信息，主要展示名称等其他属性
 *
 * Created by wanlipeng on 2020/6/10 5:43 PM
 */
class ComponentDetailFragment : Fragment() {
    // ViewModel
    private lateinit var componentDetailViewModel: ComponentDetailViewModel
    private lateinit var mListAdapter: AppDetailListAdapter
    private lateinit var binding: FragmentDetailComponentInfoBinding
    private var data: ComponentInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        componentDetailViewModel = ViewModelProvider(this)[ComponentDetailViewModel::class.java]
        binding = FragmentDetailComponentInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = arguments?.get("component") as ComponentInfo
        data?.let {
            initView(it)
            // 异步更新数据
            componentDetailViewModel.updateData(it)
        }
        // 设置监听器
        initObserver()
    }

    private fun initView(c: ComponentInfo) {
        val context = requireContext()
        binding.activityName.text = c.shortName
        binding.recyclerViewActivityInfo.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewActivityInfo.addDividerItemDecoration(
            context,
            R.drawable.inset_recyclerview_divider
        )
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.detail_component_activity_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_start_app) {
            // 启动程序
            launchApp(requireView())
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchApp(view: View) {
        data?.let { d ->
            if (d.exported) {
                Snackbar.make(view, R.string.action_launch_activity, Snackbar.LENGTH_LONG)
                    .setAction(R.string.launch) {
                        openActivity(d)
                    }.show()
            } else {
                toastLong("${d.name}未设置导出，不能调用")
            }
        }
    }

    private fun openActivity(c: ComponentInfo) {
        try {
            startActivity(IntentUtil.createLaunchActivityIntent(c.packageName!!, c.name!!))
        } catch (e: SecurityException) {
            toastLong(e.message)
        }
    }
}