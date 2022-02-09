package com.domker.app.doctor.detail.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.domker.app.doctor.R
import com.domker.app.doctor.data.SIGNATURE_MD5
import com.domker.app.doctor.data.SIGNATURE_SHA1
import com.domker.app.doctor.data.SIGNATURE_SHA256
import com.domker.app.doctor.detail.DetailItemViewHolder
import com.domker.app.doctor.entiy.AppItemInfo

/**
 * 展示签名的容器
 */
class SignatureContainer(inflater: LayoutInflater) :
    AbstractContainer<DetailItemViewHolder>(inflater, DETAIL_TYPE_SIGNATURE) {

    override fun createContainerView(parent: ViewGroup): View {
        return createView(R.layout.item_subject_content, parent)
    }

    override fun bindViewHolder(
        holder: DetailItemViewHolder,
        data: AppItemInfo,
        position: Int
    ) {
        holder.content?.text = data.content
        holder.subject?.text = data.subject

        holder.icon?.let {
            it.visibility = View.VISIBLE
            it.setImageResource(R.drawable.ic_baseline_build_24)
        }

        holder.itemView.setOnClickListener {
            // 点击打开签名弹窗，展示详细多种类型的签名
            val bundle = Bundle()
            data.signatures?.first()?.also { appSignature ->
                bundle.putString(SIGNATURE_MD5, appSignature.md5Signature)
                bundle.putString(SIGNATURE_SHA1, appSignature.sha1Signature)
                bundle.putString(SIGNATURE_SHA256, appSignature.sha256Signature)
                Navigation.findNavController(holder.view).navigate(R.id.navigation_signature_dialog, bundle)
            }
        }
    }

}