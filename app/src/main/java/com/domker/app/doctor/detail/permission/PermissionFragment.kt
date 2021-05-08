package com.domker.app.doctor.detail.permission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentDetailPermissionBinding
import com.domker.app.doctor.detail.AppDetailActivity
import com.domker.base.addItemDecoration

class PermissionFragment : Fragment() {
    private lateinit var binding: FragmentDetailPermissionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentDetailPermissionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("PermissionFragment onActivityCreated")

        AppDetailActivity.componentViewModel?.permissionInfo?.observe(viewLifecycleOwner, {
            binding.pathTitle.text = "共${it.size}条"
            binding.recyclerView.adapter = PermissionListAdapter(requireContext(), it.sortedBy { info -> info.name })
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.addItemDecoration(requireContext(), R.drawable.inset_recyclerview_divider)
            binding.recyclerView.adapter?.notifyDataSetChanged()
        })
    }

}