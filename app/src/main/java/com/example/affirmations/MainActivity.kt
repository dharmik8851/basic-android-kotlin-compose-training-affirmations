/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.affirmations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.affirmations.model.AffirmationWithImage
import com.example.affirmations.ui.theme.AffirmationsTheme
import com.example.affirmations.viewmodel.AffirmationViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            AffirmationsTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AffirmationsApp()
                }
            }
        }
    }
}

@Composable
fun AffirmationsApp(viewModel: AffirmationViewModel = viewModel()) {

    LaunchedEffect(Unit) { viewModel.fetchAffirmation() }

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AffirmationList(
            affirmationList = viewModel.affirmations.collectAsState().value,
            modifier = Modifier.padding(vertical = 8.dp),
            onFetchClick = { viewModel.fetchAffirmation() },
            isLoading = viewModel.isLoading.collectAsState().value
        )
    }
}

@Composable
fun AffirmationList(
    affirmationList: List<AffirmationWithImage>,
    modifier: Modifier = Modifier,
    onFetchClick: () -> Unit,
    isLoading: Boolean
) {
    LazyColumn(modifier = modifier) {
        items(affirmationList) { affirmation ->
            AffirmationCard(
                affirmation, modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    androidx.compose.material3.CircularProgressIndicator()
                } else {
                    Button(onClick = onFetchClick) {
                        Text("Fetch one more")
                    }
                }
            }
        }
    }

}

@Composable
fun AffirmationCard(affirmation: AffirmationWithImage, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(
        affirmation.imageUrl,
        "drawable",
        context.packageName
    )
    ElevatedCard(
        modifier = modifier, shape = MaterialTheme.shapes.extraLarge, // more “fun” rounded shape
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(android.R.color.holo_blue_bright),
            contentColor = MaterialTheme.colorScheme.onPrimary
        ), elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp, pressedElevation = 2.dp
        )
    ) {
        Column {
            Image(
                painter = painterResource(imageResId),
                contentDescription = affirmation.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = affirmation.text,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAffirmationApp() {
    AffirmationsTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            AffirmationsApp()
        }
    }
}
