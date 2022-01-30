package com.domker.app.doctor.detail.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentSignatureDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val ARG_ITEM_COUNT = "item_count"

/**
 * 展示签名详情的Fragment
 */
class SignatureDialogFragment : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentSignatureDialogBinding

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignatureDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        activity?.findViewById<RecyclerView>(R.id.rootLayout)?.layoutManager = LinearLayoutManager(context)
//        activity?.findViewById<RecyclerView>(R.id.rootLayout)?.adapter = arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it) }
    }

    private inner class ViewHolder(binding: FragmentSignatureDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        val text: TextView = binding.text
    }

}