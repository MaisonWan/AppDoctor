package com.domker.app.doctor.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.databinding.FragmentComponentDetailBinding
import com.domker.app.doctor.entiy.AppItemInfo
import com.domker.app.doctor.util.ManifestLabel
import com.domker.app.doctor.widget.AppDetailAdapter
import com.domker.app.doctor.widget.AppDetailItemDiffCallBack
import com.domker.base.addItemDecoration
import com.domker.base.thread.AppExecutors
import com.domker.base.toChinese

/**
 * 组件详细信息
 *
 * Created by wanlipeng on 2020/6/10 5:43 PM
 */
class ComponentDetailFragment : Fragment() {
    private val detailList: MutableList<AppItemInfo> = mutableListOf()
    private lateinit var componentDetailViewModel: ComponentDetailViewModel
    private lateinit var adapter: AppDetailAdapter
    private lateinit var binding: FragmentComponentDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        componentDetailViewModel = ViewModelProvider(this).get(ComponentDetailViewModel::class.java)
        binding = FragmentComponentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val p = arguments?.get("component") as ComponentInfo
        initView(p)
        initData(p)
        initObserver()
    }

    private fun initData(c: ComponentInfo) {
        val checker = AppCheckFactory.getInstance(requireContext()).checker
        AppExecutors.executor.execute {
            val detail = when (c.type) {
                ComponentInfo.TYPE_ACTIVITY -> checker.getActivityInfo(c.packageName!!, c.name!!)
                ComponentInfo.TYPE_SERVICE -> checker.getServiceInfo(c.packageName!!, c.name!!)
                ComponentInfo.TYPE_PROVIDER -> checker.getProviderInfo(c.packageName!!, c.name!!)
                ComponentInfo.TYPE_RECEIVER -> checker.getReceiverInfo(c.packageName!!, c.name!!)
                else -> checker.getActivityInfo(c.processName!!, c.name!!)
            }
            componentDetailViewModel.detail.postValue(detail)
        }
    }

    private fun initView(c: ComponentInfo) {
        val context = requireContext()
        binding.activityName.text = c.shortName
        binding.recyclerViewActivityInfo.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewActivityInfo.addItemDecoration(context, R.drawable.inset_recyclerview_divider)
        binding.recyclerViewActivityInfo.setItemViewCacheSize(100)
        adapter = AppDetailAdapter(context, AppDetailItemDiffCallBack())
        binding.recyclerViewActivityInfo.adapter = adapter
    }

    private fun initObserver() {
        componentDetailViewModel.detail.observe(viewLifecycleOwner, Observer {
            binding.appIcon.setImageDrawable(it.icon)
            binding.appPackage.text = it.name

            detailList.add(AppItemInfo("组件名称", it.name ?: ""))
            detailList.add(AppItemInfo("进程名", it.processName!!))

            when (it.type) {
                ComponentInfo.TYPE_ACTIVITY -> initActivityData(it)
                ComponentInfo.TYPE_SERVICE -> initServiceData(it)
                ComponentInfo.TYPE_PROVIDER -> initProviderData(it)
                ComponentInfo.TYPE_RECEIVER -> initReceiverData(it)
            }
            detailList.add(AppItemInfo("Enable", it.enabled.toChinese()))
            detailList.add(AppItemInfo("Export", it.exported.toChinese()))

            adapter.setDetailList(detailList)
            adapter.notifyDataSetChanged()
        })
    }

    private fun initActivityData(it: ComponentInfo) {
        detailList.add(AppItemInfo("屏幕方向", ManifestLabel.screen(it.screenOrientation), ManifestLabel.intAndHex(it.screenOrientation)))
        detailList.add(AppItemInfo("任务栈名称", it.taskAffinity!!))
        detailList.add(AppItemInfo("输入法模式", ManifestLabel.inputMethod(it.softInputMode), ManifestLabel.intAndHex(it.softInputMode)))
        detailList.add(AppItemInfo("Config", ManifestLabel.config(it.configChanges), ManifestLabel.intAndHex(it.configChanges)))
        detailList.add(AppItemInfo("Flag", ManifestLabel.activityFlag(it.flags), ManifestLabel.intAndHex(it.flags)))
    }

    private fun initServiceData(it: ComponentInfo) {
        detailList.add(AppItemInfo("权限", it.permission.toChinese()))
        detailList.add(AppItemInfo("Flag", ManifestLabel.activityFlag(it.flags), ManifestLabel.intAndHex(it.flags)))
    }

    private fun initProviderData(it: ComponentInfo) {
        detailList.add(AppItemInfo("Flag", ManifestLabel.providerFlag(it.flags), ManifestLabel.intAndHex(it.flags)))
    }

    private fun initReceiverData(it: ComponentInfo) {
        detailList.add(AppItemInfo("任务栈名称", it.taskAffinity!!))
        detailList.add(AppItemInfo("Config", ManifestLabel.config(it.configChanges), ManifestLabel.intAndHex(it.configChanges)))
        detailList.add(AppItemInfo("Flag", ManifestLabel.activityFlag(it.flags), ManifestLabel.intAndHex(it.flags)))
    }
}