package com.opentry.android_storage_samples.fileprovider.utils

import android.graphics.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun applyGrayscaleFilter(original: Bitmap): Bitmap {
    return withContext(Dispatchers.Default) {
        val height = original.height
        val width = original.width

        val modifiedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(modifiedBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply {
            setSaturation(0f)
        }
        val filter = ColorMatrixColorFilter(colorMatrix)

        paint.colorFilter = filter
        canvas.drawBitmap(original, null, Rect(0, 0, original.width, original.height), paint)

        modifiedBitmap
    }
}

fun byteArrayToBitmap(byteArray: ByteArray) =
    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
