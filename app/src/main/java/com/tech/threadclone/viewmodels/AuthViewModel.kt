package com.tech.threadclone.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tech.threadclone.models.UserModel
import com.tech.threadclone.utils.SharedRef
import java.util.UUID

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.value = null
                    _error.value =null
                    _firebaseUser.postValue(auth.currentUser)
                    getData(auth.currentUser?.uid,context)
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                } else {
                    _error.value = null
                    _error.postValue(it.exception?.localizedMessage)
                }
            }
    }

    private fun getData(uid: String?, context: Context) {
        userRef.child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userModel = snapshot.getValue(UserModel::class.java)
                SharedRef.storeData(
                    userModel?.name!!,userModel.email, userModel.bio,
                    userModel.userName, userModel.imageUrl, context )
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun register(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: String,
        context: Context
    ) {
        Log.d("register@@", "register: $imageUri")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.value = null
                    _error.value = null
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(
                        email,
                        password,
                        name,
                        bio,
                        userName,
                        imageUri,
                        auth.currentUser?.uid,
                        context
                    )
                    followersFirestoreDBCreate()
                } else {
                    _error.value = null
                    _error.postValue(it.exception?.message)
                }
            }
    }

    private fun followersFirestoreDBCreate() {
        val firestoredb = Firebase.firestore
        val followersRef = firestoredb.collection("followers").document(FirebaseAuth.getInstance().currentUser?.uid!!)
        val followingRef = firestoredb.collection("following").document(FirebaseAuth.getInstance().currentUser?.uid!!)

        followersRef.set(mapOf("followersIds" to listOf<String>()))
        followingRef.set(mapOf("followingIds" to listOf<String>()))
    }

    private fun saveImage(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: String,
        uid: String?,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imageUri.toUri())
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
               val downloadUrl = imageUrl.toString()
                saveData(email, password, name, bio, userName, downloadUrl, uid, context)
                Log.d("saveData", "SaveData: $email")
                Log.d("saveData", "SaveData: $password")
                Log.d("saveData", "SaveData: $name")
                Log.d("saveData", "SaveData: $bio")
                Log.d("saveData", "SaveData: $userName")
                Log.d("saveData", "SaveData: $imageUri")
                Log.d("saveData", "SaveData: $downloadUrl")
                Log.d("saveData", "SaveData: $uid")
            }
        }.addOnFailureListener {
            saveData(email, password, name, bio, userName, "", uid, context)
        }

    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUrl: String,
        uid: String?,
        context: Context
    ) {
        val userModel = UserModel(email, password, name, bio, userName, imageUrl, uid)
        userRef.child(uid!!).setValue(userModel)
            .addOnSuccessListener {
                SharedRef.storeData(name, email, bio, userName, imageUrl, context)
            }.addOnFailureListener {
                Toast.makeText(context, "Something went wrong.when Saving Data", Toast.LENGTH_SHORT)
                    .show()
            }
    }
    fun logout(){
        auth.signOut()
        _firebaseUser.postValue(null)
    }
}