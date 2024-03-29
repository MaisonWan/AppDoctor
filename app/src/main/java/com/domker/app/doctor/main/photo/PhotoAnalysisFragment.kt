package com.domker.app.doctor.main.photo

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentMainPhotoBinding
import com.domker.app.doctor.widget.BaseAppFragment
import com.domker.base.addDividerItemDecoration

/**
 * 展示照片分析EXIF信息的地方
 *
 * Created by wanlipeng on 2021/5/12 5:36 下午
 */
class PhotoAnalysisFragment : BaseAppFragment() {
    private lateinit var adapter: ExifListAdapter
    private lateinit var binding: FragmentMainPhotoBinding
    private lateinit var exifViewModel: ExifViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>

    // 是否已经初始化了图片展示的View
    private var isInitPhotoView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        exifViewModel = ViewModelProvider(this)[ExifViewModel::class.java]

        binding = FragmentMainPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                parsePhotoExif(result.resultCode, result.data)
            }
    }

    private fun initButton() {
        binding.imageButtonAdd.setOnClickListener {
            openPhoto()
        }

        exifViewModel.getExif().observe(viewLifecycleOwner) {
            showExif(it)
        }
    }

    private fun initPhotoView() {
        if (isInitPhotoView) {
            return
        }
        binding.recyclerViewExif.visibility = View.VISIBLE
        binding.imageViewPreview.visibility = View.VISIBLE
        binding.imageButtonAdd.visibility = View.GONE

        val recyclerViewAppInfo: RecyclerView = binding.recyclerViewExif
        recyclerViewAppInfo.layoutManager = LinearLayoutManager(context)
        recyclerViewAppInfo.addDividerItemDecoration(
            requireContext(),
            R.drawable.inset_recyclerview_divider
        )
        recyclerViewAppInfo.setItemViewCacheSize(100)
        adapter = ExifListAdapter(requireActivity())
        recyclerViewAppInfo.adapter = adapter
        isInitPhotoView = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.main_photo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_photo) {
            openPhoto()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 调用相册，返回一张照片
     */
    private fun openPhoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT  //实现相册多选
        launcher.launch(Intent.createChooser(intent, "选择图片"))
    }

    private fun parsePhotoExif(resultCode: Int, data: Intent?) {
        if (data != null) {
            initPhotoView()
            val uri = data.data ?: data.clipData?.getItemAt(0)?.uri
            uri?.let {
                Glide.with(requireContext()).load(it).into(binding.imageViewPreview)
                // 异步解析图片的信息
                exifViewModel.parserExif(requireActivity(), it)
            }
        }
    }

    /**
     * 展示信息
     */
    private fun showExif(list: List<ExifItem>) {
        adapter.setData(list)
        adapter.notifyItemRangeChanged(0, list.size)
    }
}