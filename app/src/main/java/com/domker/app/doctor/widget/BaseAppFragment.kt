package com.domker.app.doctor.widget

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.domker.app.doctor.main.AppViewModel


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
        appViewModel.includeAllApp.observe(viewLifecycleOwner) {
            onAppIncludeChanged(it)
        }
    }

    protected open fun onAppIncludeChanged(includeAll: Boolean) {
        appIncludeAll = includeAll
    }
}