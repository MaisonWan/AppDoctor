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
import com.domker.base.addDividerItemDecoration
import java.util.*

/**
 * 系统属性展示，数量比较多，增加搜索
 */
class PropertiesFragment : Fragment() {

    private lateinit var binding: FragmentDevicePropertiesBinding
    private lateinit var adapter: PropertiesAdapter
    private lateinit var data: List<Pair<String, String>>
    private val mLock = Object()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kotlin.run {
            synchronized(mLock) {
                data = DeviceScanner(requireContext()).getSystemProperties()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentDevicePropertiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        synchronized(mLock) {
            adapter = PropertiesAdapter(requireContext(), data)
        }
        binding.recyclerViewInfo.adapter = adapter
        binding.recyclerViewInfo.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewInfo.addDividerItemDecoration(requireContext(), R.drawable.inset_recyclerview_divider)
        adapter.notifyItemRangeChanged(0, data.size)

        val downData = data.map { it.first }
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
        adapter.notifyItemRangeChanged(0, data.size)
    }
    
}