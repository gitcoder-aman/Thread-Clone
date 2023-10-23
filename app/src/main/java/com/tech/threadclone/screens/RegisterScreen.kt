package com.tech.threadclone.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tech.threadclone.navigation.Routes
import com.tech.threadclone.utils.roboto_light
import com.tech.threadclone.utils.roboto_regular

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
    Column(
        modifier = Modifier
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
        Spacer(modifier = Modifier.height(50.dp))

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
            navController.navigate(Routes.Login.routes){
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
    }
}