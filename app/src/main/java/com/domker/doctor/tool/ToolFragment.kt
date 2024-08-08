package com.domker.doctor.tool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.doctor.databinding.FragmentToolBinding

/**
 * 工具的入口
 */
class ToolFragment : Fragment() {

    private lateinit var binding: FragmentToolBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentToolBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        context?.let { c ->
            binding.recyclerView.adapter = ToolLinkAdapter(c)
            binding.recyclerView.layoutManager = LinearLayoutManager(c)
        }

    }

}