package com.domker.app.doctor.widget

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.domker.app.doctor.data.AppState

/**
 * Fragment的基类
 * Created by wanlipeng on 3/5/21 4:35 PM
 */
abstract class BaseAppFragment : Fragment() {
    /**
     * 是否包含所有的app
     */
    protected var appIncludeAll = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppState.appViewModel.includeAllApp.observe(viewLifecycleOwner) {
            onAppIncludeChanged(it)
        }
    }

    protected open fun onAppIncludeChanged(includeAll: Boolean) {
        appIncludeAll = includeAll
    }
}