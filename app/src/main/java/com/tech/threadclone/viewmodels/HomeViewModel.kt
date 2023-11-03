package com.tech.threadclone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tech.threadclone.models.ThreadModel
import com.tech.threadclone.models.UserModel
class HomeViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val thread = db.getReference("threads")

    private var _threadsAndUsers = MutableLiveData<List<Pair<ThreadModel, UserModel>>>()
    val threadsAndUsers: LiveData<List<Pair<ThreadModel, UserModel>>> = _threadsAndUsers

    init {
        fetchThreadsAndUsers {
            _threadsAndUsers.value = it
        }
    }
    private fun fetchThreadsAndUsers(onResult:(List<Pair<ThreadModel,UserModel>>)->Unit) {
        thread.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<ThreadModel,UserModel>>()
                for(threadSnapshot in snapshot.children){
                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    thread.let {threadModel->
                       fetchUserFromThread(threadModel!!){
                           userModel ->  result.add(0,threadModel to userModel)
                           Log.d("@@HomeViewModel", "onDataChange: ${userModel.uid}")
                           Log.d("@@HomeViewModel", "onDataChange: ${threadModel.userId}")
                           if(result.size == snapshot.childrenCount.toInt()){
                               onResult(result)
                           }
                       }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("@@HomeViewModel", "onCancelled: Something went wrong.${error.message}" )
            }

        })
    }
    fun fetchUserFromThread(thread : ThreadModel,onResult : (UserModel) -> Unit){
        db.getReference("users").child(thread.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel :: class.java)
                    user?.let { onResult(it) }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("@@HomeViewModel", "onCancelled: Something went wrong.${error.message}" )
                }

            })
    }
}