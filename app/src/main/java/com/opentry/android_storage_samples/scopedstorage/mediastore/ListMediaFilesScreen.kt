package com.opentry.android_storage_samples.scopedstorage.mediastore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.opentry.android_storage_samples.R
import com.opentry.android_storage_samples.scopedstorage.Demos
import com.opentry.android_storage_samples.scopedstorage.HomeRoute
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@Composable
fun ListMediaFileScreen(
    navController: NavController,
    viewModel: ListMediaFilesViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val error by viewModel.errorFlow.collectAsState(null)
    val mediaQuery by viewModel.mediaQuery.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMedia()
    }

    LaunchedEffect(error) {
        error?.let { scaffoldState.snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Demos.ListMediaFiles.name)) },
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
                if (mediaQuery.loading) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.demo_list_media_loading))
                    }
                } else {
                    if (!mediaQuery.success) {
                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(stringResource(R.string.demo_list_media_failure))
                        }
                    } else {
                        if (mediaQuery.results.isEmpty()) {
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(stringResource(R.string.demo_list_media_no_files_found))
                            }
                        } else {
                            LazyVerticalGrid(
                                modifier = Modifier.fillMaxSize(),
                                columns = GridCells.Fixed(4)
                            ) {
                                items(mediaQuery.results.size) { resource ->
                                    Box(
                                        Modifier
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .background(Color.LightGray)
                                    ) {
                                        GlideImage(
                                            imageModel = { resource }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
