package com.opentry.android_storage_samples.scopedstorage.mediastore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

@ExperimentalFoundationApi
@Composable
fun AddMediaFileScreen(
    navController: NavController,
    viewModel: AddMediaFileViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val error by viewModel.errorFlow.collectAsState(null)
    val addedMedia by viewModel.addedMedia.observeAsState()

    LaunchedEffect(error) {
        error?.let { scaffoldState.snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Demos.AddMediaFile.name)) },
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
                if (addedMedia != null) {
                    MediaFilePreviewCard(addedMedia!!)
                } else {
                    IntroCard()
                }

                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    item {
                        Button(
                            modifier = Modifier.padding(16.dp),
                            onClick = { viewModel.addImage() }) {
                            Text(stringResource(R.string.demo_add_image_label))
                        }
                    }
                    item {
                        Button(
                            modifier = Modifier.padding(16.dp),
                            onClick = { viewModel.addVideo() }) {
                            Text(stringResource(R.string.demo_add_video_label))
                        }
                    }
                }
            }
        }
    )
}
