package com.domker.doctor.app.detail.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.domker.doctor.R
import com.domker.doctor.app.detail.DetailItemViewHolder
import com.domker.doctor.data.SIGNATURE_MD5
import com.domker.doctor.data.SIGNATURE_SHA1
import com.domker.doctor.data.SIGNATURE_SHA256
import com.domker.doctor.entiy.AppItemInfo

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
            data.signatures?.let { appSignatures ->
                val md5Array = arrayOfNulls<String>(appSignatures.size)
                val sha1Array = arrayOfNulls<String>(appSignatures.size)
                val sha256Array = arrayOfNulls<String>(appSignatures.size)
                // 重复次数添加到数组里面
                println("sign size = ${appSignatures.size} ${appSignatures[0].packageName}")
                repeat(appSignatures.size) { index ->
                    md5Array[index] = appSignatures[index].md5Signature
                    sha1Array[index] = appSignatures[index].sha1Signature
                    sha256Array[index] = appSignatures[index].sha256Signature
                }
                bundle.putStringArray(SIGNATURE_MD5, md5Array)
                bundle.putStringArray(SIGNATURE_SHA1, sha1Array)
                bundle.putStringArray(SIGNATURE_SHA256, sha256Array)
                Navigation.findNavController(holder.view).navigate(R.id.navigation_signature_dialog, bundle)
            }
        }
    }

}