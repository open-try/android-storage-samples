package com.opentry.android_storage_samples.fileprovider.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.opentry.android_storage_samples.databinding.FragmentGalleryBinding
import java.io.File

const val GALLERY_COLUMNS = 3

class GalleryFragment : Fragment() {
    private val viewModel by viewModels<AppViewModel>()
    private lateinit var binding: FragmentGalleryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentGalleryBinding.inflate(inflater, container, false)

        viewModel.loadImages()

        val galleryAdapter =
            GalleryAdapter { image ->
                viewImageUsingExternalApp(image)
            }

        binding.gallery.also { view ->
            view.layoutManager = GridLayoutManager(
                activity,
                GALLERY_COLUMNS
            )
            view.adapter = galleryAdapter
        }

        viewModel.images.observe(viewLifecycleOwner) { images ->
            galleryAdapter.submitList(images)
        }

        viewModel.notification.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }

        // TODO: Add popup menu https://material.io/develop/android/components/menu/
        //  https://www.techotopia.com/index.php/Working_with_the_Android_GridLayout_in_XML_Layout_Resources
        return binding.root
    }

    private fun viewImageUsingExternalApp(imageFile: File) {
        val context = requireContext()
        val authority = "${context.packageName}.fileprovider"
        val contentUri = FileProvider.getUriForFile(context, authority, imageFile)

        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
            data = contentUri
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(viewIntent)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(
                binding.root,
                "Couldn't find suitable app to display the image",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}
