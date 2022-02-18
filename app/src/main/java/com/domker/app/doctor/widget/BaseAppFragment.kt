package com.domker.app.doctor.widget

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.domker.app.doctor.main.AppViewModel
import com.domker.app.doctor.store.LaunchSetting


/**
 * Fragment的基类
 * Created by wanlipeng on 3/5/21 4:35 PM
 */
abstract class BaseAppFragment : Fragment() {
    /**
     * 是否包含所有的app
     */
    protected var appIncludeAll = false
    private var firstLoad = true
    private val appViewModel: AppViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel.getLaunchSetting().observe(viewLifecycleOwner) {
            if (appIncludeAll != it.includeAllApp) {
                onAppIncludeChanged(it.includeAllApp)
            }
            if (firstLoad) {
                onLoadLaunchSetting(it)
                firstLoad = false
            }
        }
    }

    protected open fun onAppIncludeChanged(includeAll: Boolean) {
        appIncludeAll = includeAll
    }

    /**
     * 加载启动阶段的配置，只执行一次
     */
    protected open fun onLoadLaunchSetting(launchSetting: LaunchSetting) {

    }

}