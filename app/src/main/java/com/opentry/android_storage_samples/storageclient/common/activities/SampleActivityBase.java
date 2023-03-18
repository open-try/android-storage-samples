package com.opentry.android_storage_samples.storageclient.common.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.opentry.android_storage_samples.storageclient.common.logger.Log;
import com.opentry.android_storage_samples.storageclient.common.logger.LogWrapper;

/**
 * Base launcher activity, to handle most of the common plumbing for samples.
 */
public class SampleActivityBase extends AppCompatActivity {

    public static final String TAG = "SampleActivityBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeLogging();
    }

    /**
     * Set up targets to receive log data
     */
    public void initializeLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        Log.i(TAG, "Ready");
    }
}
