package com.domker.doctor.app.detail.lib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.domker.doctor.databinding.FragmentAppLibDetailBinding
import com.domker.base.file.AppFileUtils

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

        // 获取传递的参数
        arguments?.let {
            libName = it.getString(PARAM_LIB_NAME)
            libPath = it.getString(PARAM_LIB_PATH)
            libFileSize = it.getLong(PARAM_LIB_FILE_SIZE)
            libZipSize = it.getLong(PARAM_LIB_ZIP_SIZE)
        }
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
        showBaseInfo()
    }

    /**
     * 展示基本信息
     */
    private fun showBaseInfo() {
        libName?.let {
            binding.header.textViewLibName.text = it
        }
        libPath?.let {
            binding.header.textViewLibPath.text = it
        }
        binding.header.textViewContent.text = AppFileUtils.formatFileSize(libFileSize)
        binding.header.textViewZip.text = AppFileUtils.formatFileSize(libZipSize)
    }


}