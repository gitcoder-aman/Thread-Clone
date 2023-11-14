package com.tech.threadclone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tech.threadclone.models.ThreadModel
import com.tech.threadclone.models.UserModel

class ProfileViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val threadRef = db.getReference("threads")
    private val userRef = db.getReference("users")

    private val _threads = MutableLiveData(listOf<ThreadModel>())

    val threads : LiveData<List<ThreadModel>>
        get() = _threads

    private val _users = MutableLiveData(UserModel())

    val users : LiveData<UserModel>
        get() = _users

    private val _allUser = MutableLiveData(listOf<UserModel>())

    val allUsers : LiveData<List<UserModel>>
        get() = _allUser

    init {
        fetchAllUserExistInDatabase()
    }
    fun fetchUser(uid : String){
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _users.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("profile@@", "onCancelled: ${error.message}")
            }

        })
    }
    private fun fetchAllUserExistInDatabase(){

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for (userSnapshot in snapshot.children){
                    val userModel = userSnapshot.getValue(UserModel::class.java)
                    userModel.let { user ->
                        result.add(user!!)
                    }
                }
                _allUser.postValue(result)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("profile@@", "onCancelled: ${error.message}")
            }

        })
    }

     fun fetchThreads(uid : String){
        threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList = snapshot.children.mapNotNull {
                    it.getValue(ThreadModel::class.java)
                }
                _threads.postValue(threadList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("profile@@", "onCancelled: ${error.message}")
            }

        })
    }
}