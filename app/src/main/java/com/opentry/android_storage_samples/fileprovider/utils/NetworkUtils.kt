package com.opentry.android_storage_samples.fileprovider.utils

import android.graphics.BitmapFactory
import okhttp3.ResponseBody

fun getBitmapFromResponseBody(responseBody: ResponseBody) =
    BitmapFactory.decodeStream(responseBody.byteStream())