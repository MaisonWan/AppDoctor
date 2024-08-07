package com.domker.doctor.main.applist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.domker.doctor.databinding.FragmentSystemLinkBinding
import com.domker.doctor.widget.ViewBindingFragment

/**
 * 系统链接的界面
 */
class SystemLinkFragment : ViewBindingFragment<FragmentSystemLinkBinding>() {
    private val list = mutableListOf<SystemLink>()

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
        val adapter = SystemLinkAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 4)

        adapter.setData(SystemLinkData().getData())
    }

}