package com.opentry.android_storage_samples.safdemos.imagepicker

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.opentry.android_storage_samples.databinding.FragmentImagePickerBinding

const val IMAGE_MIME_TYPE = "image/*"
const val IMAGE_PICKER_REQUEST_CODE = 1

class ImagePickerFragment : Fragment() {
    private lateinit var binding: FragmentImagePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImagePickerBinding.inflate(layoutInflater)

        binding.selectImageButton.setOnClickListener {
            selectImage()
        }

        return binding.root
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = IMAGE_MIME_TYPE
        }

        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            resultData?.data?.let { uri ->
                activity?.let {
                    binding.preview.setImageBitmap(
                        BitmapFactory.decodeStream(it.contentResolver.openInputStream(uri))
                    )
                }
            }
        }
    }
}
