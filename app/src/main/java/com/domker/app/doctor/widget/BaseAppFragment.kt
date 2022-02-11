package com.domker.app.doctor.widget

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.domker.app.doctor.main.AppViewModel
import com.domker.app.doctor.store.AppSettings
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Fragment的基类
 * Created by wanlipeng on 3/5/21 4:35 PM
 */
abstract class BaseAppFragment : Fragment() {
    /**
     * 是否包含所有的app
     */
    protected var appIncludeAll = false
    private val appViewModel: AppViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            AppSettings.getIncludeAllApp(requireContext()).collect {
                onLoadedIncludeAllAppSettings(it)
            }
        }

        appViewModel.includeAllApp.observe(viewLifecycleOwner) {
            onAppIncludeChanged(it)
        }
    }

    protected open fun onAppIncludeChanged(includeAll: Boolean) {
        appIncludeAll = includeAll
    }

    /**
     * 加载配置，子类复写的时候写一些事件
     */
    protected open fun onLoadedIncludeAllAppSettings(includeAll: Boolean) {
        appIncludeAll = includeAll
    }
}