package com.opentry.android_storage_samples;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.opentry.android_storage_samples.actionopendocument.MainActionOpenDocumentActivity;
import com.opentry.android_storage_samples.actionopendocumenttree.MainActionOpenDocumentTreeActivity;
import com.opentry.android_storage_samples.contentproviderpaging.MainContentProviderPagingActivity;
import com.opentry.android_storage_samples.filemanager.FileExplorerActivity;
import com.opentry.android_storage_samples.fileprovider.MainFileProviderActivity;
import com.opentry.android_storage_samples.mediastore.MainMediaStoreActivity;
import com.opentry.android_storage_samples.safdemos.MainSAFDemosActivity;
import com.opentry.android_storage_samples.scopedstorage.MainScopedStorageActivity;
import com.opentry.android_storage_samples.sharingshortcuts.MainSharingShortcutsActivity;
import com.opentry.android_storage_samples.storageclient.storageclient.MainStorageClientActivity;

public class MainAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        findViewById(R.id.button_01).setOnClickListener(v -> startActivity(new Intent(MainAppActivity.this, MainActionOpenDocumentActivity.class)));
        findViewById(R.id.button_02).setOnClickListener(v -> startActivity(new Intent(MainAppActivity.this, MainActionOpenDocumentTreeActivity.class)));
        findViewById(R.id.button_03).setOnClickListener(v -> startActivity(new Intent(MainAppActivity.this, MainContentProviderPagingActivity.class)));
        findViewById(R.id.button_04).setOnClickListener(v -> Toast.makeText(MainAppActivity.this, "NO", Toast.LENGTH_SHORT).show());
        findViewById(R.id.button_05).setOnClickListener(v -> startActivity(new Intent(MainAppActivity.this, FileExplorerActivity.class)));
        findViewById(R.id.button_06).setOnClickListener(v -> startActivity(new Intent(MainAppActivity.this, MainFileProviderActivity.class)));
        findViewById(R.id.button_07).setOnClickListener(v -> startActivity(new Intent(MainAppActivity.this, MainMediaStoreActivity.class)));
        findViewById(R.id.button_08).setOnClickListener(v -> startActivity(new Intent(MainAppActivity.this, MainSAFDemosActivity.class)));
        findViewById(R.id.button_09).setOnClickListener(v -> startActivity(new Intent(MainAppActivity.this, MainScopedStorageActivity.class)));
        findViewById(R.id.button_10).setOnClickListener(v -> startActivity(new Intent(MainAppActivity.this, MainSharingShortcutsActivity.class)));
        findViewById(R.id.button_11).setOnClickListener(v -> startActivity(new Intent(MainAppActivity.this, MainStorageClientActivity.class)));
    }
}
