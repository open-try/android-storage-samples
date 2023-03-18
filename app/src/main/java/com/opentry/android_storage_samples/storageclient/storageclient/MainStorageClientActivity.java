package com.opentry.android_storage_samples.storageclient.storageclient;

import android.os.Bundle;
import android.view.Menu;

import androidx.fragment.app.FragmentTransaction;

import com.opentry.android_storage_samples.R;
import com.opentry.android_storage_samples.storageclient.common.activities.SampleActivityBase;
import com.opentry.android_storage_samples.storageclient.common.logger.Log;
import com.opentry.android_storage_samples.storageclient.common.logger.LogFragment;
import com.opentry.android_storage_samples.storageclient.common.logger.LogWrapper;
import com.opentry.android_storage_samples.storageclient.common.logger.MessageOnlyLogFilter;

/**
 * A simple launcher activity containing a summary sample description
 * and a few action bar buttons.
 */
public class MainStorageClientActivity extends SampleActivityBase {

    public static final String TAG = "MainStorageClientActivity";

    public static final String FRAGTAG = "StorageClientFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_storage_client);

        if (getSupportFragmentManager().findFragmentByTag(FRAGTAG) == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            StorageClientFragment fragment = new StorageClientFragment();
            transaction.add(fragment, FRAGTAG);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_storage_client, menu);
        return true;
    }

    /**
     * Create a chain of targets that will receive log data
     */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());
        logFragment.getLogView().setTextAppearance(this, R.style.Log);


        Log.i(TAG, "Ready");
    }
}
