package com.domker.app.doctor.detail.lib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.domker.app.doctor.databinding.FragmentAppLibDetailBinding

const val PARAM_LIB_NAME = "lib_name"
const val PARAM_LIB_PATH = "lib_path"
const val PARAM_LIB_FILE_SIZE = "lib_file_size"
const val PARAM_LIB_ZIP_SIZE = "lib_zip_size"

/**
 * 展示so lib的详细信息
 */
class AppLibDetailFragment : Fragment() {
    private lateinit var binding: FragmentAppLibDetailBinding
    private var libName: String? = null
    private var libPath: String? = null
    private var libFileSize: Long = 0
    private var libZipSize: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppLibDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}