package com.example.jetareader.screens.update

import MBook
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.jetareader.R
import com.example.jetareader.components.InputField
import com.example.jetareader.components.RatingBar
import com.example.jetareader.components.ReaderAppBar
import com.example.jetareader.components.RoundedButton
import com.example.jetareader.data.DataOrException
import com.example.jetareader.navigation.ReaderScreens
import com.example.jetareader.screens.home.HomeScreenViewModel
import com.example.jetareader.util.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
@ExperimentalComposeUiApi
@Composable
fun ReaderBookUpdateScreen(navController: NavController, bookItemId: String,
                           viewModel: HomeScreenViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Update Book",

                navController = navController
            )
        }
    ) { innerPadding ->
        val bookInfo = produceState<DataOrException<List<MBook>,Boolean,Exception>>(
            initialValue = DataOrException(data = emptyList(), true, Exception(""))
        ){
            value = viewModel.data.value
        }.value

        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(3.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(bookInfo.loading == true){
                   LinearProgressIndicator()
                    bookInfo.loading = false
                }else{
                    Surface(modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(),
                        shape = CircleShape,
                        shadowElevation = 4.dp) {
                        ShowBookUpdate(
                            bookInfo = viewModel.data.value,
                            bookItemId = bookItemId
                        )
                    }
                }

                viewModel.data.value.data?.let { books ->
                    val book = books.firstOrNull { mBook ->
                        mBook.googleBookId == bookItemId
                    }
                    if (book != null) {
                        ShowSimpleForm(book = book, navController,viewModel)
                    } else {
                        Text("Kitap bulunamadı")
                    }
                }

            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun ShowSimpleForm(book: MBook, navController: NavController,viewModel: HomeScreenViewModel) {

    val context = LocalContext.current

    val notesText = remember {
        mutableStateOf("")
    }

    val isStartedReading = remember {
        mutableStateOf(false)
    }

    val isFinishedReading = remember{
        mutableStateOf(false)
    }

    val ratingVal = remember {
        mutableStateOf(0)
    }

    SimpleForm(defaultValue = if(book.notes.toString().isNotEmpty()) book.notes.toString() else "No thoughts available") { note ->
        notesText.value = note
    }

    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        // Start Reading Button
        TextButton(
            onClick = {
                viewModel.updateStartedReading(book)
                isStartedReading.value = true
            },
            enabled = !isStartedReading.value
        ) {
            Text(
                text = if (isStartedReading.value) "Started Reading!" else "Start Reading",
                style = MaterialTheme.typography.bodyLarge
            )
        }

// Finish Reading Button
        TextButton(
            onClick = {
                viewModel.updateFinishedReading(book)
                isFinishedReading.value = true
            },
            enabled = isStartedReading.value && !isFinishedReading.value
        ) {
            Text(
                text = if (isFinishedReading.value) "Finished Reading" else "Mark as Read",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))

    book.rating?.toInt().let {
        RatingBar(
            rating = it!!
        ) { rating ->
            ratingVal.value = rating
        }
    }

    Spacer(modifier = Modifier.padding(bottom = 25.dp))
    Row {

        val changedNotes = book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.value
        val isFinishedTimeStamp = if (isFinishedReading.value) Timestamp.now() else book.finishedReading
        val isStartedTimeStamp = if (isStartedReading.value) Timestamp.now() else book.startedReading

        val bookUpdate = changedNotes || changedRating || isStartedReading.value || isFinishedReading.value

        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value).toMap()

        RoundedButton(label = "Update"){
            if (bookUpdate) {
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .update(bookToUpdate)
                    .addOnCompleteListener {
                        showToast(context = context, "Book Updated Successfully!")
                        viewModel.getAllBooksFromDatabase()
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)

                    }.addOnFailureListener{
                        Log.w("Error", "Error updating document" , it)
                    }
            }
        }
        Spacer(modifier = Modifier.width(100.dp))
        val openDialog = remember {
            mutableStateOf(false)
        }
        if (openDialog.value) {
            ShowAlertDialog(message = stringResource(id = R.string.sure) + "\n" +
                    stringResource(id = R.string.action), openDialog){
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            openDialog.value = false

                            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                        }
                    }
            }
        }

        RoundedButton("Delete"){
            openDialog.value = true
        }
    }
}
@Composable
fun ShowAlertDialog(message: String, openDialog: MutableState<Boolean>, onYesPressed:  () -> Unit) {
    if(openDialog.value){
        AlertDialog(
            onDismissRequest = {openDialog.value = false},
            title = { Text(text = "Delete Book") },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(
                    onClick = {onYesPressed()}
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {openDialog.value = false}
                ) {
                    Text(text = "No")
                }
            }
        )
    }
}


fun showToast(context: Context, msg: String) {
    Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
}

@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    defaultValue: String,
    onValueChange: (String) -> Unit
) {
    val textFieldValue = remember {
        mutableStateOf(defaultValue)
    }

    Column {
        InputField(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            valueState = textFieldValue,
            labelId = "Enter Your Thoughts",
            enabled = true,
            onActions = KeyboardActions {
                onValueChange(textFieldValue.value)
            }
        )

        // Dışarıdan gelen değişiklikleri textFieldValue'ya uygula
        LaunchedEffect(defaultValue) {
            textFieldValue.value = defaultValue
        }
        // Her değişimde dışarıya bildir
        LaunchedEffect(textFieldValue.value) {
            onValueChange(textFieldValue.value)
        }
    }
}


@Composable
fun ShowBookUpdate(bookInfo: DataOrException<List<MBook>, Boolean, Exception>, bookItemId: String) {

    Row(

    ) {
        Spacer(modifier = Modifier.width(43.dp))

        if(bookInfo.data != null){
            Column(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                CardListItem(book = bookInfo.data!!.first { mBook -> mBook.googleBookId == bookItemId }, onPressDetails = {})
            }
        }
    }
}
@Composable
fun CardListItem(book: MBook, onPressDetails: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {  },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    book.photoUrl  ?: "BOOK_NO_PHOTO"
                ),
                contentDescription = "Book Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(120.dp)
                    .height(100.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(topStart = 120.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 0.dp))
            )
            Column() {
                Text(text = book.title.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp).width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)

                Text(text = book.authors.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp,).width(120.dp))

                Text(text = book.publishedDate.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp,).width(120.dp))
            }
        }

    }
}