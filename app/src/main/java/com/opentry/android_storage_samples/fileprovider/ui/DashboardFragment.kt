package com.opentry.android_storage_samples.fileprovider.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.opentry.android_storage_samples.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private val viewModel by viewModels<AppViewModel>()
    private lateinit var binding: FragmentDashboardBinding

    private val takePicture = registerForActivityResult(TakePicturePreview()) { bitmap ->
        bitmap?.let { viewModel.saveImageFromCamera(it) }
    }

    private val selectPicture = registerForActivityResult(GetContentWithMimeTypes()) { uri ->
        uri?.let {
            viewModel.copyImageFromUri(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        binding.takePicture.setOnClickListener {
            takePicture.launch(null)
        }

        binding.selectPicture.setOnClickListener {
            selectPicture.launch(ACCEPTED_MIMETYPES)
        }

        binding.addRandomImage.setOnClickListener {
            viewModel.saveRandomImageFromInternet()
        }

        binding.clearFiles.setOnClickListener {
            viewModel.clearFiles()
        }

        viewModel.notification.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }
}

class GetContentWithMimeTypes : ActivityResultContract<Array<String>, Uri?>() {
    override fun createIntent(
        context: Context,
        input: Array<String>
    ): Intent {
        return Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType("*/*")
            .putExtra(Intent.EXTRA_MIME_TYPES, input)

    }

    override fun getSynchronousResult(
        context: Context,
        input: Array<String>
    ): SynchronousResult<Uri?>? {
        return null
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
    }
}