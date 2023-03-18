package com.opentry.android_storage_samples.fileprovider.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.util.*

enum class Source {
    CAMERA, FILEPICKER, INTERNET;

    override fun toString(): String {
        return name.lowercase(Locale.getDefault())
    }
}

fun generateFilename(source: Source) = "$source-${System.currentTimeMillis()}.jpg"

suspend fun copyImageFromStream(input: InputStream, directory: File) {
    withContext(Dispatchers.IO) {
        input.copyTo(File(directory, generateFilename(Source.FILEPICKER)).outputStream())
    }
}