package com.tech.threadclone.screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.tech.threadclone.R
import com.tech.threadclone.navigation.Routes
import com.tech.threadclone.utils.roboto_light
import com.tech.threadclone.utils.roboto_regular
import com.tech.threadclone.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    var bio by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<String>("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val authViewModel : AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val context = LocalContext.current
    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else android.Manifest.permission.READ_EXTERNAL_STORAGE

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri.toString()
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
            isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission failed", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(firebaseUser){
        if(firebaseUser != null) {
            isLoading = false
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create an Account", style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = roboto_regular,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = if (imageUri.isEmpty()) painterResource(id = R.drawable.man)
                else rememberAsyncImagePainter(model = imageUri),
                contentDescription = "person",
                modifier = Modifier
                    .size(96.dp)
                    .clip(
                        CircleShape
                    )
                    .background(Color.LightGray)
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED
                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(value = name, onValueChange = {
                name = it
            }, label = {
                Text(
                    text = "Name", style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W200,
                        fontFamily = roboto_light,
                        color = Color.Black
                    )
                )
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
            )
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

            OutlinedTextField(value = bio, onValueChange = {
                bio = it
            }, label = {
                Text(
                    text = "Bio", style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W200,
                        fontFamily = roboto_light,
                        color = Color.Black
                    )
                )
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(value = username, onValueChange = {
                username = it
            }, label = {
                Text(
                    text = "Username", style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W200,
                        fontFamily = roboto_light,
                        color = Color.Black
                    )
                )
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ElevatedButton(
                onClick = {
                    if (name.isEmpty() || email.isEmpty() || password.isEmpty() || bio.isEmpty() || username.isEmpty()) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Please fill all text box."
                            )
                        }
                    }else{
                        isLoading = true
                        Log.d("@@register", "RegisterScreen: $imageUri")
                        authViewModel.register(email,password,name,bio,username, imageUri,context)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Create Account", style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W200,
                        fontFamily = roboto_regular,
                        color = Color.White
                    ), modifier = Modifier.padding(vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = {
                navController.navigate(Routes.Login.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }) {
                Text(
                    text = "Already Created! Login here.", style = TextStyle(
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