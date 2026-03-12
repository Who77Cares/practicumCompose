package com.example.practicumcompose

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


/*
Реализации тобаров тут достано кривые
 */

class TabsLesson {
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScaffLesson() {
    Scaffold(topBar = {}) {
        TopAppBar(
            backgroundColor = Color.Blue,
            title = {
                Text("What")
            },
            navigationIcon =  {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Build,
                        contentDescription = null
                    )
                }
            }

        )
    }
}

@Composable
fun TabsTopBar() {
    val tabs = listOf("A", "B", "C")
    var selected = remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Column(
            ) {
                TopAppBar(
                    backgroundColor = Color.Red,
                    title = { Text("Табы") },
                    actions = {
                        IconButton({}) { Icon(Icons.Default.MoreVert, null) }
                    }
                )
                ScrollableTabRow(selected.value) {
                    tabs.forEachIndexed { i, title ->
                        Tab(
                            selected = i == selected.value,
                            onClick = { selected.value = i },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues).fillMaxSize()) {
            Text("Выбрано: ${tabs[selected.value]}")
        }
    }
}

@Composable
fun SearchTopBar() {
    var isSearching = remember { mutableStateOf(false) }
    var query = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearching.value) {
                        BasicTextField(
                            value = query.value,
                            onValueChange = { query.value = it },
                            decorationBox = { innerTextField ->
                                Box(Modifier.fillMaxWidth()) {
                                    if (query.value.isEmpty()) Text("Поиск...")
                                    innerTextField()
                                }
                            }
                        )
                    } else Text("Заметки")
                },
                navigationIcon = {
                    if (isSearching.value) {
                        IconButton({ isSearching.value = false }) {
                            Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                        }
                    }
                },
                actions = {
                    if (!isSearching.value) {
                        IconButton({ isSearching.value = true }) {
                            Icon(Icons.Default.Search, null)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues).fillMaxSize()) {
            Text("Контент")
        }
    }
}
