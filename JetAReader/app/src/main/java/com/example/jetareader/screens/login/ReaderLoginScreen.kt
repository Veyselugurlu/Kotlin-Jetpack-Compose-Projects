package com.example.jetareader.screens.login

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jetareader.R
import com.example.jetareader.components.EmailInput
import com.example.jetareader.components.PasswordInput
import com.example.jetareader.components.ReaderLogo
import com.example.jetareader.navigation.ReaderNavigation
import com.example.jetareader.navigation.ReaderScreens

@Composable
fun ReaderLoginScreen(navController: NavController,
                      viewModel: LoginScreenViewModel= viewModel()
){
    val showLoginForm = rememberSaveable() { mutableStateOf(true) }

    Surface(modifier = Modifier
        .padding(1.dp)
        .fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
                ReaderLogo()
           if (showLoginForm.value) UserForm(loading = false, isCreateAccount = false){ email,password ->
              viewModel.signInWithEmailAndPassword(email,password){
                  navController.navigate(ReaderScreens.ReaderHomeScreen.name)
              }
           }
           else{
               UserForm(loading = false, isCreateAccount = true){ email,password ->
                   viewModel.createUserWithEmailAndPassword(email,password){
                       navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                   }
               }
           }
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                val text = if (showLoginForm.value) "Sign up" else "Login"
                Text("New User?")
                Text(text, modifier = Modifier.clickable {
                    showLoginForm.value = !showLoginForm.value
                }.padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}

@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String,String) -> Unit = {email, pwd ->}
){
    val email = rememberSaveable() { mutableStateOf(" ") }
    val password = rememberSaveable() { mutableStateOf("") }
    val passwordVisibility = rememberSaveable() { mutableStateOf(false) }
    val passworFocusRequest = FocusRequester.Default
    val keyBoardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .padding(16.dp)
        .height(230.dp)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        if (isCreateAccount) Text(stringResource(R.string.create_acct),
           modifier = Modifier.padding(5.dp)) else Text("")

        EmailInput(
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions{
            passworFocusRequest.requestFocus()
        })

        PasswordInput(
            modifier = Modifier.focusRequester(passworFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions{
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(),password.value.trim())
            },)
        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else " Login",
            loading=loading,
            validInputs=valid
        ){
            onDone(email.value.trim(),password.value.trim())
            keyBoardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(textId: String,
                 loading: Boolean,
                 validInputs: Boolean,
                 onClick: ()-> Unit) {
        Button(onClick=onClick,
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            enabled = !loading && validInputs,
            shape = CircleShape) {
            if (loading) CircularProgressIndicator(
                modifier = Modifier.size(25.dp))
            else Text(textId, modifier = Modifier.padding(5.dp))
        }
}
