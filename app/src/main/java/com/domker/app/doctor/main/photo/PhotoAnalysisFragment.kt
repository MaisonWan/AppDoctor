package com.domker.app.doctor.main.photo

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentMainPhotoBinding
import com.domker.app.doctor.util.DataFormat
import com.domker.app.doctor.widget.BaseAppFragment
import com.domker.base.addDividerItemDecoration
import com.domker.base.thread.AppExecutors

const val ACTION_CHOOSE_PHOTO = 200

/**
 * 展示照片分析EXIF信息的地方
 *
 * Created by wanlipeng on 2021/5/12 5:36 下午
 */
class PhotoAnalysisFragment : BaseAppFragment() {
    private lateinit var adapter: ExifListAdapter
    private lateinit var binding: FragmentMainPhotoBinding
    private lateinit var exifViewModel: ExifViewModel

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
        exifViewModel = ViewModelProvider(this).get(ExifViewModel::class.java)

        binding = FragmentMainPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
    }

    private fun initButton() {
        binding.imageButtonAdd.setOnClickListener {
            openPhoto()
        }

        exifViewModel.exif.observe(viewLifecycleOwner) {
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
        startActivityForResult(Intent.createChooser(intent, "选择图片"), ACTION_CHOOSE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_CHOOSE_PHOTO && data != null) {
            initPhotoView()
            val uri = if (data.data != null) {
                data.data
            } else {
                data.clipData?.getItemAt(0)?.uri
            }
            uri?.let {
                Glide.with(requireContext()).load(it).into(binding.imageViewPreview)
                AppExecutors.executor.execute {
                    val stream = requireActivity().contentResolver.openInputStream(it)
                    val parser = PhotoExifParser(stream!!)
                    val exif = parser.loadExif()
                    exifViewModel.exif.postValue(exif)
                }
            }

        }
    }

    /**
     * 展示信息
     */
    private fun showExif(exif: PhotoExif) {
        val data = mutableListOf<ExifItem>()
        data.add(ExifItem("EXIF版本", exif.exifVersion))
        data.add(ExifItem("分辨率", "${exif.width} x ${exif.height}px"))
        data.add(ExifItem("分辨率单元", exif.resolutionUnit.toString()))
        data.add(ExifItem("X像素密度", exif.xResolution.toString()))
        data.add(ExifItem("Y像素密度", exif.yResolution.toString()))

        data.add(ExifItem("软件名称", exif.software))
        data.add(ExifItem("拍摄时间", DataFormat.getFormatFullDate(exif.camera.dateTime)))
        data.add(ExifItem("数字化时间", DataFormat.getFormatFullDate(exif.camera.dateTimeDigitized)))
        data.add(ExifItem("设备型号", exif.camera.model))
        data.add(ExifItem("设备品牌", exif.camera.make))
        data.add(ExifItem("焦距", "${exif.camera.focalLength} mm"))
        data.add(ExifItem("测光模式", DataFormat.meteringMode(exif.camera.meteringMode)))
        data.add(ExifItem("光圈", "f/${exif.camera.aperture}"))
        data.add(ExifItem("曝光时长", DataFormat.getDoubleShort(exif.camera.exposureTime) + "s"))
        data.add(ExifItem("曝光补偿", exif.camera.exposureBiasValue.toString()))
        data.add(ExifItem("曝光模式", DataFormat.exposureMode(exif.camera.exposureProgram)))
        data.add(ExifItem("ISO感光度", exif.camera.ios.toString()))
        data.add(ExifItem("色彩空间", DataFormat.colorSpace(exif.colorSpace)))

        data.add(ExifItem("白平衡", exif.camera.whiteBalance))
        data.add(ExifItem("闪光灯", exif.camera.flash.toString()))
        data.add(ExifItem("旋转角度", "${exif.camera.orientation}°"))
        data.add(ExifItem("GPS定位", "", TYPE_LOCATION, exif.gps))

        data.add(ExifItem("GPS时间", DataFormat.getFormatFullDate(exif.gps.gpsDateTime)))
        data.add(ExifItem("海拔高度", "${exif.gps.altitudeRef}${exif.gps.altitude} M"))

        adapter.setData(data)
        adapter.notifyDataSetChanged()
    }
}