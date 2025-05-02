package com.example.noteapp.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteInputText(
    modifier : Modifier=Modifier,
    text:String,
    label:String,
    maxLine:Int= 1,
    onTextChange:(String) -> Unit,
    onImeAction: () -> Unit = {},
    ){
    val keyboardController = LocalSoftwareKeyboardController.current
    //assagida butun bu ozellikleri kullanıp esnek bir textfield olusturdum.
    TextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent),
        maxLines = maxLine,
        label={ Text(text=label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction= ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide()

        }),
        modifier = modifier
    )
}

@Composable
fun NoteButton(
    modifier: Modifier=Modifier,
    text:String,
    color: Color = MaterialTheme.colorScheme.outline,
    onClick: () -> Unit,
    enabled: Boolean=true,
){
    Button(
        onClick = onClick,
        shape = CircleShape,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        modifier = modifier
    ) {
        Text(text)
    }
}