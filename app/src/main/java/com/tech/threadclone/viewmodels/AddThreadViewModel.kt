package com.tech.threadclone.viewmodels

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tech.threadclone.models.ThreadModel
import java.util.UUID

class AddThreadViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("threads")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

     fun saveImage(
        threadText: String,
        userId: String,
        imageUri: Uri,
        context : Context
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
               val downloadUrl = imageUrl.toString()
                saveData(threadText,userId,downloadUrl,context)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Image Upload Failed: $it", Toast.LENGTH_SHORT).show()
        }

    }

     fun saveData(
        threadText: String,
        userId: String,
        downloadUrl: String,
        context : Context
    ) {
        val addThreadModel  = ThreadModel(threadText,downloadUrl,userId,System.currentTimeMillis().toString())
        userRef.child(userRef.push().key!!).setValue(addThreadModel)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }.addOnFailureListener {
                _isPosted.postValue(false)
                Toast.makeText(context, "Something went wrong.when Saving Data", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}