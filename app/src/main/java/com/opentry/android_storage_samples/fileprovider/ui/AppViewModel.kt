package com.opentry.android_storage_samples.fileprovider.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.XmlResourceParser
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.opentry.android_storage_samples.R
import com.opentry.android_storage_samples.fileprovider.utils.Source
import com.opentry.android_storage_samples.fileprovider.utils.applyGrayscaleFilter
import com.opentry.android_storage_samples.fileprovider.utils.copyImageFromStream
import com.opentry.android_storage_samples.fileprovider.utils.generateFilename
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

private const val FILEPATH_XML_KEY = "files-path"
private const val RANDOM_IMAGE_URL = "https://source.unsplash.com/random/500x500"
val ACCEPTED_MIMETYPES = arrayOf("image/jpeg", "image/png")

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val httpClient by lazy { OkHttpClient() }

    private val context: Context
        get() = getApplication()

    private val _notification = MutableLiveData<String>()
    val notification: LiveData<String>
        get() = _notification

    private val imagesFolder: File by lazy { getImagesFolder(context) }

    private val _images = MutableLiveData(emptyList<File>())
    val images: LiveData<List<File>>
        get() = _images

    fun loadImages() {
        viewModelScope.launch {
            val images = withContext(Dispatchers.IO) {
                imagesFolder.listFiles()!!.toList()
            }

            _images.postValue(images)
        }
    }

    fun saveImageFromCamera(bitmap: Bitmap) {
        val imageFile = File(imagesFolder, generateFilename(Source.CAMERA))
        val imageStream = FileOutputStream(imageFile)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val grayscaleBitmap = withContext(Dispatchers.Default) {
                        applyGrayscaleFilter(bitmap)
                    }
                    grayscaleBitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageStream)
                    imageStream.flush()
                    imageStream.close()

                    _notification.postValue("Camera image saved")

                } catch (e: Exception) {
                    Log.e(javaClass.simpleName, "Error writing bitmap", e)
                }
            }
        }
    }

    @SuppressLint("Recycle")
    fun copyImageFromUri(uri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.contentResolver.openInputStream(uri)?.let {
                    // TODO: Apply grayscale filter before saving image
                    copyImageFromStream(it, imagesFolder)
                    _notification.postValue("Image copied")
                }
            }
        }
    }

    fun saveRandomImageFromInternet() {
        viewModelScope.launch {
            val request = Request.Builder().url(RANDOM_IMAGE_URL).build()

            withContext(Dispatchers.IO) {
                val response = httpClient.newCall(request).execute()

                response.body?.let { responseBody ->
                    val imageFile = File(imagesFolder, generateFilename(Source.INTERNET))
                    // TODO: Apply grayscale filter before saving image
                    imageFile.writeBytes(responseBody.bytes())
                    _notification.postValue("Image downloaded")
                }

                if (!response.isSuccessful) {
                    _notification.postValue("Failed to download image")
                }
            }
        }
    }

    fun clearFiles() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                imagesFolder.deleteRecursively()
                _images.postValue(emptyList())
                _notification.postValue("Images cleared")
            }
        }
    }
}

private fun getImagesFolder(context: Context): File {
    val xml = context.resources.getXml(R.xml.provider_paths)

    val attributes = getAttributesFromXmlNode(xml, FILEPATH_XML_KEY)

    val folderPath = attributes["path"]
        ?: error("You have to specify the sharable directory in res/xml/provider_paths.xml")

    return File(context.filesDir, folderPath).also {
        if (!it.exists()) {
            it.mkdir()
        }
    }
}

// TODO: Make the function suspend
private fun getAttributesFromXmlNode(
    xml: XmlResourceParser,
    stringNodeName: String
): Map<String, String> {
    while (xml.eventType != XmlResourceParser.END_DOCUMENT) {
        if (xml.eventType == XmlResourceParser.START_TAG) {
            if (xml.name == stringNodeName) {
                if (xml.attributeCount == 0) {
                    return emptyMap()
                }

                val attributes = mutableMapOf<String, String>()

                for (index in 0 until xml.attributeCount) {
                    attributes[xml.getAttributeName(index)] = xml.getAttributeValue(index)
                }

                return attributes
            }
        }

        xml.next()
    }

    return emptyMap()
}
