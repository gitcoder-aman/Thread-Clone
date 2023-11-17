package com.tech.threadclone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
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

    private val _followersList = MutableLiveData(listOf<String>())

    val followersList : LiveData<List<String>>
        get() = _followersList

    private val _followingList = MutableLiveData(listOf<String>())

    val followingList : LiveData<List<String>>
        get() = _followingList

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
    private val firestoreDb = Firebase.firestore
    fun followUsers(userId : String,currentUserId : String){
        val followingRef = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)
        followingRef.update("followingIds", FieldValue.arrayUnion(userId))
        followerRef.update("followersIds",FieldValue.arrayUnion(currentUserId))
    }
    fun getFollowers(userId: String){
        firestoreDb.collection("followers").document(userId)
            .addSnapshotListener{ value, _ ->
                val followerIds = value?.get("followersIds") as? List<String> ?: listOf()
                _followersList.postValue(followerIds)
            }
    }
    fun getFollowing(userId: String){
        firestoreDb.collection("following").document(userId)
            .addSnapshotListener{ value, _ ->
                val followingIds = value?.get("followingIds") as? List<String> ?: listOf()
                _followingList.postValue(followingIds)
            }
    }
    fun unFollowUser(userId: String,currentUserId: String){
        val followingRef = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        followingRef.update("followingIds",FieldValue.arrayRemove(userId)).addOnCompleteListener {
            Log.d("unfollow@@", "unfollow successul")
        }.addOnFailureListener {
            Log.d("unfollow@@", "fail unfollow", it)
        }
        followerRef.update("followersIds",FieldValue.arrayRemove(currentUserId)).addOnCompleteListener {
            Log.d("unfollow@@", "unfollow successul")
        }.addOnFailureListener {
            Log.d("unfollow@@", "fail unfollow", it)
        }
//        followingRef.get().addOnCompleteListener {
//            if(it.isSuccessful){
//                val task = it.result
//                if(task.exists()){
//
//                }else{
//                    Log.d("unfollow@@", "No Such Document", it.exception);
//                }
//            }else{
//                Log.w("unfollow@@", "Error getting document", it.exception);
//            }
//        }
//        followerRef.update("followersIds",currentUserId,null).addOnCompleteListener {
//            if(it.isSuccessful){
//                Log.d("unfollow@@", "Field successfully deleted!")
//            }else{
//                Log.d("unfollow@@", "Error updating field", it.exception)
//            }
//        }
//        followerRef.get().addOnCompleteListener {
//            if(it.isSuccessful){
//                val task = it.result
//                if(task.exists()){
//
//                }else{
//                    Log.d("unfollow@@", "No Such Document", it.exception);
//                }
//            }else{
//                Log.w("unfollow@@", "Error getting document", it.exception);
//            }
//        }
    }
}