package com.example.jetareader.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetareader.components.InputField
import com.example.jetareader.components.ReaderAppBar
import com.example.jetareader.model.Item
import com.example.jetareader.navigation.ReaderScreens
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun ReaderSearchScreen(navController: NavController,
                       viewModel: BookSearchViewModel = hiltViewModel()
){
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Search Books",
            icon = Icons.Default.ArrowBackIosNew,
            navController = navController,
            showProfile = false){
            //  navController.popBackStack()
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }
    }) { innerPadding->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Column(){
                SearchForm(modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)){ searchQuery->
                    viewModel.searchBooks(query=searchQuery)
                }
                BookList(navController,viewModel)
            }
        }
    }
}

@Composable
fun BookList(navController: NavController,
             viewModel: BookSearchViewModel = hiltViewModel()) {

    val listOfBooks = viewModel.list
    if (viewModel.isLoading){
        LinearProgressIndicator()
    }else{
    LazyColumn(modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp)) {
        items(items = listOfBooks){ book ->
            BookRow(book,navController)}
        }
    }
}

@Composable
fun BookRow(book: Item,
            navController: NavController) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreens.DetailScreen.name+"/${book.id}")
        }
        .fillMaxWidth()
        .height(130.dp)
        .padding(bottom = 10.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(5.dp),
        ) {
        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top,
            ) {
            val imageUrl: String = book.volumeInfo.imageLinks?.smallThumbnail
                ?: "http://books.google.com/books/content?id=kyylDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"


            Image(rememberImagePainter(data = imageUrl),
                                        contentDescription = "book image",
                                        modifier = Modifier
                                            .width(80.dp)
                                            .fillMaxHeight()
                                            .padding(end = 4.dp))
            Column(modifier = Modifier.padding(5.dp)) {
                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                Text(text = "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyLarge)
                Text(text = "Date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyLarge)
                Text(text = "${book.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@ExperimentalComposeUiApi
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    val searchQueryState = rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }

    InputField(
        valueState = searchQueryState,
        labelId = "Search",
        enabled = true,
        modifier = modifier,
    )

    LaunchedEffect(searchQueryState.value) {
        searchJob?.cancel()
        val query = searchQueryState.value.trim()
        if (query.isNotEmpty()) {
            searchJob = coroutineScope.launch {
                delay(500)
                onSearch(query)
            }
        }
    }
}
