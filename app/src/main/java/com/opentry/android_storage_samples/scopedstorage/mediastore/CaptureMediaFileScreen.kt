package com.opentry.android_storage_samples.scopedstorage.mediastore

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.opentry.android_storage_samples.R
import com.opentry.android_storage_samples.scopedstorage.Demos
import com.opentry.android_storage_samples.scopedstorage.HomeRoute
import com.opentry.android_storage_samples.scopedstorage.common.IntroCard
import com.opentry.android_storage_samples.scopedstorage.common.MediaFilePreviewCard
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun CaptureMediaFileScreen(
    navController: NavController,
    viewModel: CaptureMediaFileViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val error by viewModel.errorFlow.collectAsState(null)
    val capturedMedia by viewModel.capturedMedia.observeAsState()

    val scope = rememberCoroutineScope()
    var targetImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var targetVideoUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        targetImageUri?.let {
            viewModel.onImageCapture(it)
            targetImageUri = null
        }
    }
    val takeVideo = rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) {
        targetVideoUri?.let {
            viewModel.onVideoCapture(it)
            targetVideoUri = null
        }
    }


    LaunchedEffect(error) {
        error?.let { scaffoldState.snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Demos.CaptureMediaFile.name)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack(HomeRoute, false) }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_label)
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                if (capturedMedia != null) {
                    MediaFilePreviewCard(capturedMedia!!)
                } else {
                    IntroCard()
                }

                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    item {
                        Button(
                            modifier = Modifier.padding(16.dp),
                            onClick = {
                                scope.launch {
                                    viewModel.createImageUri()?.let {
                                        targetImageUri = it
                                        takePicture.launch(it)
                                    }
                                }
                            }
                        ) {
                            Text(stringResource(R.string.demo_capture_image_label))
                        }
                    }
                    item {
                        Button(
                            modifier = Modifier.padding(16.dp),
                            onClick = {
                                scope.launch {
                                    viewModel.createVideoUri()?.let {
                                        targetVideoUri = it
                                        takeVideo.launch(it)
                                    }
                                }
                            }
                        ) {
                            Text(stringResource(R.string.demo_capture_video_label))
                        }
                    }
                }
            }
        }
    )
}