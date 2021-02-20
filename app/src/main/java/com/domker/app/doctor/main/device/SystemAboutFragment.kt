package com.domker.app.doctor.main.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.domker.app.doctor.R

/**
 * 关于本机的页面
 */
class SystemAboutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_system_about, container, false)
    }

    companion object {
        /**
         * 创建新的实例
         */
        @JvmStatic
        fun newInstance() = SystemAboutFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}