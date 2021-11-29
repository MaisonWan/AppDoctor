package com.domker.app.doctor.battery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.domker.app.doctor.battery.databinding.BatteryFragmentLayoutBinding

/**
 * 电池展示信息的界面
 */
class BatteryFragment : Fragment() {
    private lateinit var binding: BatteryFragmentLayoutBinding
    private lateinit var batteryWatcher: BatteryWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        batteryWatcher = BatteryWatcher(requireContext())
        batteryWatcher.init(this) { battery ->

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BatteryFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

}
