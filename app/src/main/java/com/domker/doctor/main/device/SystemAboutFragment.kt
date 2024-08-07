package com.domker.doctor.main.device

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.doctor.databinding.FragmentSystemAboutBinding
import com.domker.base.addDividerItemDecoration
import com.domker.base.device.DeviceManager
import com.domker.doctor.R

private const val REQUEST_PERMISSION = 101

/**
 * 关于本机的页面
 */
class SystemAboutFragment : Fragment() {
    private lateinit var binding: FragmentSystemAboutBinding
    private lateinit var deviceManager: DeviceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentSystemAboutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceManager = DeviceManager(requireContext()).also {
            if (it.checkPermission()) {
                val state = it.getDeviceAbout()
                initDataShow(state)
            } else {
                requestPermissions(it.getPermissions(), REQUEST_PERMISSION)
            }
        }
    }

    private fun initDataShow(state: List<Pair<String, String>>) {
        PropertiesAdapter(requireContext(), state).also {
            binding.recyclerView.adapter = it
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.addDividerItemDecoration(requireContext(), R.drawable.inset_recyclerview_divider)
        }.notifyItemRangeChanged(0, state.size)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            grantResults.forEachIndexed { index, g ->
                if (g != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "需要${permissions[index]}授权才能使用本页面功能", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            initDataShow(deviceManager.getDeviceAbout())
        }
    }

    companion object {
        /**
         * 创建新的实例
         */
        @JvmStatic
        fun newInstance() = SystemAboutFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}