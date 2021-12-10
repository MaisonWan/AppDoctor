package com.domker.app.doctor.sensor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.domker.app.doctor.hardware.databinding.FragmentSensorBinding

/**
 * 传感器相关的测试界面，主要展示多种传感器类型，并且在每个页面都有独立的展示和测试
 */
class SensorFragment : Fragment() {

    private lateinit var binding: FragmentSensorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SensorWatcher(requireContext()).addRegister()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSensorBinding.inflate(inflater, container, false)

        return binding.root
    }

}