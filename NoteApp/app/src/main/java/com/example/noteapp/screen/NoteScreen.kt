package com.example.noteapp.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.R
import com.example.noteapp.components.NoteButton
import com.example.noteapp.components.NoteInputText
import com.example.noteapp.data.NotesDataSource
import com.example.noteapp.model.Note
import com.example.noteapp.util.formatDate

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    notes: List<Note>,
    onAddNote:(Note) -> Unit,
    onRemoveNote:(Note) ->Unit
){
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    Column() {
        //AppBar
        TopAppBar(
            title = {
            Text(text= stringResource(R.string.app_name))
                    },
            actions = {
                Icon(imageVector = Icons.Rounded.Notifications,
                    contentDescription = "Icon")
        },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Blue,
                titleContentColor = Color.White,
        ))

        //Content
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            NoteInputText(
                modifier = Modifier.padding(12.dp),
                text = title,
                "title",
                onTextChange = {
                    if (it.all{ char->
                            char.isLetter() || char.isWhitespace()
                        }) title = it
                })
            NoteInputText(
                modifier = Modifier.padding(12.dp),
                text= description,
                label = "Add a Note",
                onTextChange = {
                    if (it.all{ char->
                            char.isLetter() || char.isWhitespace()
                        }) description = it
                })
            NoteButton(
                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp * 0.4f),
                text = "Save",
                onClick = {
                    if(title.isNotEmpty() && description.isNotEmpty()){
                        //ekleme
                    onAddNote(Note(
                        title = title,
                        description = description))
                    title=""
                    description=""
                        Toast.makeText(context,"Note Added",
                            Toast.LENGTH_SHORT).show()
                }
                },
                )
        }

        HorizontalDivider(modifier=Modifier.padding(10.dp))
        LazyColumn {
            items(notes){ note->
                NoteRow(note=note,
                 onNoteClicked = {
                     onRemoveNote(note)
                 })
            }
        }
    }
}

@Composable
fun NoteRow(
    modifier:Modifier=Modifier,
    note:Note,
    onNoteClicked:(Note)->Unit){
    Surface(modifier.padding(4.dp)
        .clip(shape = RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp))
        .fillMaxWidth(),
            color =MaterialTheme.colorScheme.secondaryContainer,
            ) {
        Column(modifier
            .clickable {onNoteClicked(note) }
            .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.Start) {
            Text(text = note.title, style = MaterialTheme.typography.headlineLarge)
            Text(text = note.description, style = MaterialTheme.typography.bodyLarge)
            Text(text = formatDate(note.entryDate.time) ,style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview(){
    NoteScreen(notes = NotesDataSource().loadNotes(), onAddNote ={}, onRemoveNote = {})
}
