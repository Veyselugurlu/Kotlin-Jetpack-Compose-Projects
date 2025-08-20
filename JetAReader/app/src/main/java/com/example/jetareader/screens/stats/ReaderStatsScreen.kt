package com.example.jetareader.screens.stats

import MBook
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetareader.components.ReaderAppBar
import com.example.jetareader.model.Item
import com.example.jetareader.navigation.ReaderScreens
import com.example.jetareader.screens.home.HomeScreenViewModel
import com.example.jetareader.screens.search.BookRow
import com.example.jetareader.util.formatDate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.combine
import java.util.Locale

@Composable
fun ReaderStatsScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()){

        var books: List<MBook>
        val currentUser = FirebaseAuth.getInstance().currentUser
        Scaffold(topBar = {
                ReaderAppBar(
                        "Book Stats",
                        icon = Icons.Default.ArrowBackIosNew,
                        showProfile = false,
                        navController = navController){
                                navController.popBackStack()
                        }

        },) { innerpadding->
                Surface(modifier = Modifier
                        .padding(innerpadding)) {
                        Column(modifier = Modifier.padding(1.dp)) {
                        books = if (!viewModel.data.value.data.isNullOrEmpty()){
                                viewModel.data.value.data!!.filter { mBook ->
                                        (mBook.userId == currentUser?.uid)
                                }
                        }else{
                                emptyList()
                        }
                             Row(){
                                Box(modifier = Modifier
                                        .size(45.dp)
                                        .padding(2.dp)){
                                        Icon(imageVector = Icons.Sharp.Person,
                                                contentDescription = "icon")
                                }
                                     //veysel @me.com
                                     Text("Hi, ${currentUser?.email.toString().split("@")[0].uppercase(
                                             Locale.getDefault())}",  modifier = Modifier.padding(bottom = 16.dp))
                            }
                        Card(modifier = Modifier
                                .fillMaxWidth()
                                .padding(3.dp),
                                shape = CircleShape,
                                elevation = CardDefaults.cardElevation(1.dp)) {
                                val readBookList : List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()) {
                                        books.filter { mBook ->
                                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                                        }
                        }else{
                                emptyList()
                                }

                                val readingBooks = books.filter { mBook ->
                                        (mBook.startedReading != null && mBook.finishedReading == null)
                                }
                                Column(modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                                        horizontalAlignment = Alignment.Start) {
                                        Text("Your Stats", style = MaterialTheme.typography.titleLarge)
                                        HorizontalDivider(
                                                thickness = 1.dp,
                                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                        )
                                        Text("You are reading: ${readingBooks.size} books")
                                        Text("You've read: ${readBookList.size} books")
                                }
                        }
                                if (viewModel.data.value.loading == true) {
                                        LinearProgressIndicator()
                                }else{
                                        HorizontalDivider(
                                                thickness = 1.dp,
                                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                        )
                                        LazyColumn(modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(),
                                                contentPadding = PaddingValues(16.dp)) {
                                                // filter book by finished ones
                                                val readBooks: List<MBook> =
                                                        if (!viewModel.data.value.data.isNullOrEmpty()) {
                                                            viewModel.data.value.data!!.filter {mBook ->
                                                                    (mBook.userId == currentUser?.uid) && (mBook.finishedReading !=null)
                                                            }
                                                        }else{
                                                                emptyList()
                                                        }
                                                items(items = readBooks){ book ->
                                                        BookRowStats(book = book)
                                                }

                                        }
                                }
                }
        }

}}

@Composable
fun BookRowStats(
        book: MBook) {
        Card(modifier = Modifier
                .clickable {
                     //   navController.navigate(ReaderScreens.DetailScreen.name+"/${book.id}")
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
                        val imageUrl: String = if( book.photoUrl.toString().isEmpty())
                                "http://books.google.com/books/content?id=kyylDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
                        else{
                                book.photoUrl.toString()
                }
                        Image(
                                rememberImagePainter(data = imageUrl),
                                contentDescription = "book image",
                                modifier = Modifier
                                        .width(80.dp)
                                        .fillMaxHeight()
                                        .padding(end = 4.dp))
                        Column(modifier = Modifier
                                .padding(5.dp)) {

                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                                        if (book.rating!! >= 4){
                                                Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                                                Icon(Icons.Default.ThumbUp,
                                                        contentDescription = "Thumbs Up",
                                                        tint = Color.Green.copy(0.5f))
                                        }else{
                                                Box{}
                                        }
                                }


                                Text(text = "Author: ${book.authors}",
                                        overflow = TextOverflow.Clip,
                                        fontStyle = FontStyle.Italic,
                                        style = MaterialTheme.typography.bodyLarge)

                                Text(text = "Started: ${formatDate(book.startedReading!!)}",
                                        softWrap = true,
                                        overflow = TextOverflow.Clip,
                                        fontStyle = FontStyle.Italic,
                                        style = MaterialTheme.typography.bodyLarge)

                                Text(text = "Finished: ${formatDate(book.finishedReading!!)}",
                                        overflow = TextOverflow.Clip,
                                        fontStyle = FontStyle.Italic,
                                        style = MaterialTheme.typography.bodyLarge)
                        }
                }
        }
}