package com.opentry.android_storage_samples.scopedstorage.saf

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
import com.opentry.android_storage_samples.scopedstorage.common.DocumentFilePreviewCard
import com.opentry.android_storage_samples.scopedstorage.common.IntroCard

const val GENERIC_MIMETYPE = "*/*"
const val PDF_MIMETYPE = "application/pdf"
const val ZIP_MIMETYPE = "application/zip"

@ExperimentalFoundationApi
@Composable
fun SelectDocumentFileScreen(
    navController: NavController,
    viewModel: SelectDocumentFileViewModel = viewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val error by viewModel.errorFlow.collectAsState(null)
    val selectedFile by viewModel.selectedFile.observeAsState()

    val selectFile =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let { viewModel.onFileSelect(it) }
        }

    LaunchedEffect(error) {
        error?.let { scaffoldState.snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Demos.SelectDocumentFile.name)) },
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
                if (selectedFile != null) {
                    DocumentFilePreviewCard(selectedFile!!)
                } else {
                    IntroCard()
                }

                LazyVerticalGrid(columns = GridCells.Fixed(1)) {
                    item {
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = { selectFile.launch(arrayOf(GENERIC_MIMETYPE)) }) {
                            Text(stringResource(R.string.demo_select_any_document))
                        }
                    }
                    item {
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = { selectFile.launch(arrayOf(PDF_MIMETYPE)) }) {
                            Text(stringResource(R.string.demo_select_pdf_document))
                        }
                    }
                    item {
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = { selectFile.launch(arrayOf(ZIP_MIMETYPE)) }) {
                            Text(stringResource(R.string.demo_select_zip_document))
                        }
                    }
                }
            }
        }
    )
}
