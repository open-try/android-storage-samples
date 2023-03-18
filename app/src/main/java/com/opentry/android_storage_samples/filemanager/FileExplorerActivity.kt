package com.onwaystudio.file_manager

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.onwaystudio.file_manager.databinding.ActivityFileExplorerBinding
import java.io.File

const val MANAGE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 1
const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 2

class FileExplorerActivity : AppCompatActivity() {
    private var hasPermission = false
    private lateinit var binding: ActivityFileExplorerBinding
    private lateinit var currentDirectory: File
    private lateinit var filesList: List<File>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFileExplorerBinding.inflate(layoutInflater)
        binding.toolbar.inflateMenu(R.menu.file_manager_menu)
        setContentView(binding.root)

        setupUi()
    }

    override fun onResume() {
        super.onResume()

        hasPermission = checkStoragePermission(this)
        if (hasPermission) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                if (!Environment.isExternalStorageLegacy()) {
                    binding.rationaleView.visibility = View.GONE
                    binding.legacyStorageView.visibility = View.VISIBLE
                    return
                }
            }

            binding.rationaleView.visibility = View.GONE
            binding.filesTreeView.visibility = View.VISIBLE

            // TODO: Use getStorageDirectory instead https://developer.android.com/reference/android/os/Environment.html#getStorageDirectory()
            open(getExternalStorageDirectory())
        } else {
            binding.rationaleView.visibility = View.VISIBLE
            binding.filesTreeView.visibility = View.GONE
        }
    }

    private fun setupUi() {
        binding.toolbar.setOnMenuItemClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }

        binding.permissionButton.setOnClickListener {
            requestStoragePermission(this)
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        binding.filesTreeView.adapter = adapter
        binding.filesTreeView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = filesList[position]
            open(selectedItem)
        }
    }

    private fun open(selectedItem: File) {
        if (selectedItem.isFile) {
            return openFile(this, selectedItem)
        }

        currentDirectory = selectedItem
        filesList = getFilesList(currentDirectory)

        adapter.clear()
        adapter.addAll(filesList.map {
            if (it.path == selectedItem.parentFile.path) {
                renderParentLink(this)
            } else {
                renderItem(this, it)
            }
        })

        adapter.notifyDataSetChanged()
    }
}
