package com.domker.doctor.main.device

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.domker.doctor.base.addDividerItemDecoration
import com.domker.doctor.base.device.DeviceManager
import com.domker.doctor.R
import com.domker.doctor.databinding.FragmentSystemStateBinding

private const val request_permission = 101

/**
 * 系统状态信息
 */
class SystemStateFragment : Fragment() {
    private lateinit var binding: FragmentSystemStateBinding
    private lateinit var deviceManager: DeviceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSystemStateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceManager = DeviceManager(requireContext()).also {
            if (it.checkPermission()) {
                val state = it.getDeviceState()
                initDataShow(state)
            } else {
//                it.getPermissions().forEach { p ->
//                    if (shouldShowRequestPermissionRationale(p)) {
//
//                    } else {
//                    }
//                }
                requestPermissions(it.getPermissions(), request_permission)
            }
        }
    }

    private fun initDataShow(state: List<Pair<String, String>>) {
        PropertiesAdapter(requireContext(), state).also {
            binding.recyclerView.adapter = it
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.addDividerItemDecoration(
                requireContext(),
                R.drawable.inset_recyclerview_divider
            )
        }.notifyItemRangeChanged(0, state.size)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == request_permission) {
            grantResults.forEachIndexed { index, g ->
                if (g != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        requireContext(),
                        "需要${permissions[index]}授权才能使用本页面功能",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
            }
            initDataShow(deviceManager.getDeviceState())
        }
    }

    companion object {
        /**
         *
         * @return A new instance of fragment SystemStateFragment.
         */
        @JvmStatic
        fun newInstance() = SystemStateFragment()
    }
}