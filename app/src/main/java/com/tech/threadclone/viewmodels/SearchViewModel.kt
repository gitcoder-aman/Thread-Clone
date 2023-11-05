package com.tech.threadclone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tech.threadclone.models.ThreadModel
import com.tech.threadclone.models.UserModel
class SearchViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val users = db.getReference("users")

    private var _userList = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>> = _userList

    init {
        fetchUser {
            _userList.value = it
        }
    }
    private fun fetchUser(onResult:(List<UserModel>)->Unit) {
        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for(userSnapshot in snapshot.children){
                    val userModel = userSnapshot.getValue(UserModel::class.java)
                    if(userModel?.uid != FirebaseAuth.getInstance().uid)
                    result.add(userModel!!)
                }
                onResult(result)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("@@HomeViewModel", "onCancelled: Something went wrong.${error.message}" )
            }

        })
    }
}