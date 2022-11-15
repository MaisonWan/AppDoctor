package com.domker.app.doctor.main.applist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.domker.app.doctor.databinding.FragmentSystemLinkBinding
import com.domker.app.doctor.widget.ViewBindingFragment

/**
 * 系统链接的界面
 */
class SystemLinkFragment : ViewBindingFragment<FragmentSystemLinkBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSystemLinkBinding {
        return FragmentSystemLinkBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
//        binding.recyclerView.adapter =
    }
}