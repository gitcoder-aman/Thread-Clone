package com.tech.threadclone.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tech.threadclone.navigation.Routes
import com.tech.threadclone.utils.roboto_light
import com.tech.threadclone.utils.roboto_regular
import com.tech.threadclone.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val error by authViewModel.error.observeAsState(null)

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            isLoading = false
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
    error?.let {message->
        isLoading = false
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login", style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = roboto_regular,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(value = email, onValueChange = {
                email = it
            }, label = {
                Text(
                    text = "Email", style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W200,
                        fontFamily = roboto_light,
                        color = Color.Black
                    )
                )
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(value = password, onValueChange = {
                password = it
            }, label = {
                Text(
                    text = "Password", style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W200,
                        fontFamily = roboto_light,
                        color = Color.Black
                    )
                )
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            ElevatedButton(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Please fill all text box."
                            )
                        }
                    } else {
                        isLoading = true
                        authViewModel.login(email, password, context)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Login", style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W200,
                        fontFamily = roboto_regular,
                        color = Color.White
                    ), modifier = Modifier.padding(vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = {
                navController.navigate(Routes.Register.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }) {
                Text(
                    text = "New User? Create Account", style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W200,
                        fontFamily = roboto_light,
                        color = Color.Black
                    )
                )
            }
            if (isLoading) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}