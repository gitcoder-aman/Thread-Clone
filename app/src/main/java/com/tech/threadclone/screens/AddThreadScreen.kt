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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.firebase.auth.FirebaseAuth
import com.tech.threadclone.R
import com.tech.threadclone.navigation.Routes
import com.tech.threadclone.utils.SharedRef
import com.tech.threadclone.utils.roboto_regular
import com.tech.threadclone.viewmodels.AddThreadViewModel

@Composable
fun AddThreadScreen(navHostController: NavHostController) {

    val threadViewModel: AddThreadViewModel = viewModel()
    val isPosted by threadViewModel.isPosted.observeAsState(false)

    val context = LocalContext.current
    var threadText by remember {
        mutableStateOf("")
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var interactionSource = remember { MutableInteractionSource() }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else android.Manifest.permission.READ_EXTERNAL_STORAGE

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission failed", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(isPosted) {
        if (isPosted) {
            threadText = ""
            imageUri = null
            Toast.makeText(context, "Thread Added", Toast.LENGTH_SHORT).show()
            navHostController.navigate(Routes.Home.routes) {
                popUpTo(Routes.AddThread.routes) {
                    inclusive = true   //remove from backstack
                }
            }
        }
    }
    ConstraintLayout(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {

        val (crossPic, text, logo, userName, editText, attachMedia,
            replyText, button, imageBox, divider) = createRefs()

        Image(painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = "close",
            modifier = Modifier
                .padding(16.dp)
                .constrainAs(crossPic) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clickable {
                    navHostController.navigate(Routes.Home.routes) {
                        popUpTo(Routes.AddThread.routes) {
                            inclusive = true   //remove from backstack
                        }
                    }
                })
        Text(
            text = "New Thread", style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            ), modifier = Modifier.constrainAs(text) {
                top.linkTo(crossPic.top)
                start.linkTo(crossPic.end)
                bottom.linkTo(crossPic.bottom)
            }
        )
        Divider(modifier = Modifier.constrainAs(divider) {
            top.linkTo(text.bottom, margin = 12.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
        Image(painter = rememberAsyncImagePainter(
            model = SharedRef.getImageUrl(context), placeholder = painterResource(
                id = R.drawable.man
            ), error = painterResource(id = R.drawable.man)
        ), contentDescription = "image",
            modifier = Modifier
                .padding(start = 16.dp)
                .constrainAs(logo) {
                    top.linkTo(divider.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                }
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = SharedRef.getUserName(context), style = TextStyle(
                fontSize = 18.sp
            ), modifier = Modifier.constrainAs(userName) {
                top.linkTo(logo.top)
                start.linkTo(logo.end, margin = 10.dp)
            }
        )
        BasicTextFieldWithHint(
            hint = "Start a thread...",
            value = threadText,
            onValueChange = { threadText = it },
            modifier = Modifier
                .padding(end = 70.dp)
                .constrainAs(editText) {
                    top.linkTo(userName.bottom)
                    start.linkTo(userName.start)
                }
                .fillMaxWidth()
        )

        if (imageUri == null) {
            Image(painter = painterResource(id = R.drawable.gallery_image),
                contentDescription = "gallery",
                alignment = Alignment.CenterStart,
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 16.dp)
                    .constrainAs(attachMedia) {
                        top.linkTo(editText.bottom)
                        start.linkTo(editText.start)
                    }
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED
                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    })
        } else {
            Box(modifier = Modifier
                .padding(top = 4.dp, end = 70.dp)
                .constrainAs(imageBox) {
                    top.linkTo(editText.bottom)
                    start.linkTo(editText.start)
                }
                .clickable {
                    val isGranted = ContextCompat.checkSelfPermission(
                        context, permissionToRequest
                    ) == PackageManager.PERMISSION_GRANTED
                    if (isGranted) {
                        launcher.launch("image/*")
                    } else {
                        permissionLauncher.launch(permissionToRequest)
                    }
                }) {

                val model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
                    .placeholder(R.drawable.placeholder_image)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build()
                val painter = rememberAsyncImagePainter(model, placeholder = painterResource(id = R.drawable.logo))
                Image(
                    painter = painter,
                    contentDescription = "imageBox",
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = "Remove Image.",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable {
                            imageUri = null
                        }, tint = Color.Black
                )
            }
        }
        Text(
            text = "Anyone can reply", style = TextStyle(
                fontSize = 14.sp
            ), modifier = Modifier.constrainAs(replyText) {
                bottom.linkTo(parent.bottom, margin = 12.dp)
                start.linkTo(parent.start, margin = 12.dp)
            }
        )
        TextButton(onClick = {
            if(imageUri == null && threadText.isEmpty()){
                return@TextButton
            } else if (imageUri == null) {
                threadViewModel.saveData(
                    threadText,
                    FirebaseAuth.getInstance().currentUser?.uid!!, "", context
                )
            } else {
                threadViewModel.saveImage(
                    threadText, FirebaseAuth.getInstance().currentUser?.uid!!,
                    imageUri!!, context
                )
            }
        }, modifier = Modifier
            .constrainAs(button) {
                end.linkTo(parent.end, margin = 12.dp)
                bottom.linkTo(parent.bottom, margin = 8.dp)
            }
            .shadow(
                elevation = 2.dp, shape = RoundedCornerShape(32.dp), clip = true,
                ambientColor = Color.Transparent, spotColor = Color.Transparent
            )
            .background(if (threadText.isNotEmpty() || imageUri != null) Color.Black else Color.Gray)
        ) {
            Text(
                text = "Post", style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White
                ), modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun BasicTextFieldWithHint(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {

    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(
                text = hint, style = TextStyle(
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
        BasicTextField(
            value = value, onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )
    }

}