package com.domker.app.doctor.detail.permission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentDetailPermissionBinding
import com.domker.app.doctor.detail.component.ComponentViewModel
import com.domker.base.addDividerItemDecoration

class PermissionFragment : Fragment() {
    private lateinit var binding: FragmentDetailPermissionBinding
    private val componentViewModel: ComponentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailPermissionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        componentViewModel.getPermissionInfo().observe(viewLifecycleOwner, {
            binding.pathTitle.text = "共${it.size}条"
            binding.recyclerView.adapter = PermissionListAdapter(requireContext(), it.sortedBy { info -> info.name })
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.addDividerItemDecoration(requireContext(), R.drawable.inset_recyclerview_divider)
            binding.recyclerView.adapter?.notifyDataSetChanged()
        })
    }

}