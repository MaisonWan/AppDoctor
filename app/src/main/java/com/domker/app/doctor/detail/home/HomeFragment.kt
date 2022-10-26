package com.domker.app.doctor.detail.home

import android.R.attr.path
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.ComponentActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentDetailMainBinding
import com.domker.app.doctor.detail.AppDetailListAdapter
import com.domker.app.doctor.detail.AppDetailViewModel
import com.domker.base.addDividerItemDecoration
import com.domker.base.file.StoragePermissionUtil
import com.google.android.material.snackbar.Snackbar
import java.io.File


/**
 * App详情信息里面的首页
 */
class HomeFragment : Fragment(), MenuProvider {
    private val detailViewModel: AppDetailViewModel by activityViewModels()
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentDetailMainBinding
    private lateinit var mListAdapter: AppDetailListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StoragePermissionUtil.registerForActivityResult(requireActivity())
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.fragment = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtonListener()
        initView()
        initObserver()
        initMenu()
    }

    private fun initView() {
        val context = requireContext()

        // recyclerview
        binding.recyclerViewAppInfo.also { r ->
            r.layoutManager = LinearLayoutManager(context)
            r.addDividerItemDecoration(context, R.drawable.inset_recyclerview_divider)
            r.setItemViewCacheSize(100)
            mListAdapter = AppDetailListAdapter(context)
            r.adapter = mListAdapter
        }
    }

    private fun initButtonListener() {
        val context = requireContext()
        binding.buttonLayout.buttonStart.setOnClickListener {
            launchApp(it, context)
        }

        binding.buttonLayout.buttonApkExplorer.setOnClickListener {
            homeViewModel.openPackageExplorer()
        }

        binding.buttonLayout.buttonUninstall.setOnClickListener {
            uninstallApp(it)
        }

        binding.buttonLayout.buttonSettings.setOnClickListener {
            homeViewModel.openAppSetting()
        }

    }

    private fun initMenu() {
        (activity as ComponentActivity).addMenuProvider(this, viewLifecycleOwner)
    }

    private fun uninstallApp(view: View) {
        Snackbar.make(view, R.string.action_uninstall_app, Snackbar.LENGTH_LONG)
            .setAction(R.string.uninstall) {
                homeViewModel.uninstallApp()
            }.show()
    }

    /**
     * 启动App
     */
    private fun launchApp(view: View, context: Context): Boolean {
        Snackbar.make(view, R.string.action_launch_app, Snackbar.LENGTH_LONG)
            .setAction(R.string.launch) {
                homeViewModel.launchApp()
            }.show()
        return true
    }


    private fun menuActions(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_start_app -> launchApp(requireView(), requireContext())
            R.id.menu_uninstall_app -> uninstallApp(requireView())
            R.id.menu_setting_app -> homeViewModel.openAppSetting()
            R.id.menu_explorer_app -> homeViewModel.openPackageExplorer()
            R.id.menu_export_app -> exportApp()
            R.id.menu_share_app -> homeViewModel.shareApp(requireContext())
        }
        return true
    }

    /**
     * 导出APK到指定位置
     */
    private fun exportApp() {
        StoragePermissionUtil.requestStoragePermission(requireActivity()) {
            homeViewModel.exportApp()
        }
    }

    private fun initObserver() {
        detailViewModel.getAppInfo().observe(viewLifecycleOwner) {
            homeViewModel.bindIntroduceDetail(it.appEntity, binding)
            // 点击图标的动作
            binding.appIcon.setOnClickListener { v ->
                homeViewModel.openIconView(it.appEntity, v)
            }
            mListAdapter.setDetailList(it.itemList)
            mListAdapter.notifyItemRangeChanged(0, it.itemList.size)
        }

        // 导出文件的进度条
        homeViewModel.observerExport(viewLifecycleOwner, binding.progressBarExport)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.detail_home_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return menuActions(menuItem)
    }
}
