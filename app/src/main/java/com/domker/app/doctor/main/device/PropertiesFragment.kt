package com.domker.app.doctor.main.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentDevicePropertiesBinding
import com.domker.app.doctor.util.DeviceScanner
import com.domker.base.addItemDecoration

/**
 * 系统属性展示，数量比较多，增加搜索
 */
class PropertiesFragment : Fragment() {

    private lateinit var binding: FragmentDevicePropertiesBinding
    private lateinit var adapter: PropertiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentDevicePropertiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = DeviceScanner(requireContext()).getSystemProperties()
        adapter = PropertiesAdapter(requireContext(), data)
        binding.recyclerViewInfo.adapter = adapter
        binding.recyclerViewInfo.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewInfo.addItemDecoration(requireContext(), R.drawable.inset_recyclerview_divider)
        adapter.notifyDataSetChanged()

        val downData = data.map { PropertiesAdapter.unpackKeyValue(it)[0] }
        val downAdapter = PropertyAutoCompleteAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, downData)
        binding.autoCompleteTextView.setAdapter(downAdapter)
        // 键盘点击Search按钮之后的操作
        binding.autoCompleteTextView.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.autoCompleteTextView.dismissDropDown()
                search()
                true
            } else {
                false
            }
        }
        binding.imageButtonSearch.setOnClickListener {
            search()
        }
    }

    private fun search() {
        val keyWords = binding.autoCompleteTextView.text.toString()
        adapter.search(keyWords)
        adapter.notifyDataSetChanged()
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PropertiesFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
                }
    }
}