package com.domker.doctor.widget

import android.os.Bundle
import android.view.*
import androidx.activity.ComponentActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.domker.doctor.main.AppViewModel
import com.domker.doctor.store.LaunchSetting


/**
 * Fragment的基类
 * Created by wanlipeng on 3/5/21 4:35 PM
 */
abstract class ViewBindingFragment<T : ViewBinding> : Fragment(), MenuProvider {
    /**
     * 布局的ViewBinding
     */
    protected lateinit var binding: T
    /**
     * 是否包含所有的app
     */
    protected var appIncludeAll = false

    /**
     * 在View创建完之后，加载配置
     */
    protected var loadConfigOnViewCreated = false

    private var firstLoad = true
    private val appViewModel: AppViewModel by activityViewModels()

    /**
     * 创建ViewBinding
     */
    abstract fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = onCreateViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (loadConfigOnViewCreated) {
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
    }

    /**
     * 在创建View之后自动加载配置
     */
    protected fun autoloadConfigOnViewCreated() {
        loadConfigOnViewCreated = true
    }

    protected open fun onAppIncludeChanged(includeAll: Boolean) {
        appIncludeAll = includeAll
    }

    /**
     * 加载启动阶段的配置，只执行一次
     */
    protected open fun onLoadLaunchSetting(launchSetting: LaunchSetting) {

    }

    /**
     * 增加Menu Provider到Activity，意味声明创建菜单
     */
    protected fun addMenuProvider() {
        (activity as ComponentActivity).addMenuProvider(this, this, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}