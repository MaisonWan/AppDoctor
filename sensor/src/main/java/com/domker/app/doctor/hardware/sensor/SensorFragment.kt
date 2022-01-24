package com.domker.app.doctor.hardware.sensor

import android.hardware.Sensor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.app.doctor.hardware.R
import com.domker.app.doctor.hardware.databinding.FragmentSensorListBinding
import com.domker.base.addDividerItemDecoration

/**
 * 传感器相关的测试界面，主要展示多种传感器类型，并且在每个页面都有独立的展示和测试
 */
class SensorFragment : Fragment() {

    private lateinit var binding: FragmentSensorListBinding
    private lateinit var sensorWatcher: SensorWatcher
    private lateinit var sensorViewModel: SensorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorWatcher = SensorWatcher(requireContext())
        sensorViewModel = ViewModelProvider(this, SensorViewModel.SensorFactory(sensorWatcher))[SensorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSensorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 创建监听器
        sensorViewModel.allSensorList.observe(viewLifecycleOwner) {
            updateSensorList(it)
        }
        sensorViewModel.updateSensorData()
    }

    /**
     * 更新列表
     */
    private fun updateSensorList(sensorList: List<Sensor>) {
        binding.title.text = getString(R.string.sensor_size, sensorList.size)
        binding.recyclerView.adapter = SensorListAdapter(requireContext(), sensorList.sortedBy { it.type })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addDividerItemDecoration(requireContext(), R.drawable.inset_recyclerview_divider)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}