package com.domker.app.doctor.detail.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.domker.app.doctor.data.SIGNATURE_MD5
import com.domker.app.doctor.data.SIGNATURE_SHA1
import com.domker.app.doctor.data.SIGNATURE_SHA256
import com.domker.app.doctor.databinding.FragmentSignatureDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * 展示签名详情的Fragment
 */
class SignatureDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSignatureDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignatureDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val md5 = arguments?.getStringArray(SIGNATURE_MD5)
        val sha1 = arguments?.getStringArray(SIGNATURE_SHA1)
        val sha256 = arguments?.getStringArray(SIGNATURE_SHA256)

        binding.textViewMd5.text = md5?.get(0) ?: ""

        binding.textViewSHA1.text = sha1?.get(0) ?: ""

        binding.textViewSHA256.text = sha256?.get(0) ?: ""
    }

}