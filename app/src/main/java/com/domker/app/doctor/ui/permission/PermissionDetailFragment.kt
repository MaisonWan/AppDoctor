package com.domker.app.doctor.ui.permission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.domker.app.doctor.databinding.FragmentPermissionDetailBinding

const val PERMISSION_NAME = "name"
const val DESCRIPTION = "description"
const val PERMISSION_DETAIL = "detail"
const val GROUP_NAME = "group_name"

/**
 * 权限详情
 */
class PermissionDetailFragment : Fragment() {
    private var name: String? = null
    private var description: String? = null
    private var detail: String? = null
    private var groupName: String? = null
    private lateinit var binding: FragmentPermissionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(PERMISSION_NAME)
            description = it.getString(DESCRIPTION)
            detail = it.getString(PERMISSION_DETAIL)
            groupName = it.getString(GROUP_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPermissionDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name?.apply {
            binding.textViewName.text = this
        }
        description?.apply {
            binding.textViewDescription.text = this
        }
        detail?.apply {
            binding.textViewDetail.text = this
        }
        groupName?.apply {
            binding.textViewGroupName.text = this
        }
    }
}