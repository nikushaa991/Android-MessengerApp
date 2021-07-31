package ge.nnasaridze.messengerapp.shared.repositories.users

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface UsersRepository {
    fun getUser(id: String): Flow<Result<UserDTO>>
}

class DefaultUsersRepository : UsersRepository {


    private val database = Firebase.database.reference

    @ExperimentalCoroutinesApi
    override fun getUser(id: String) = callbackFlow<Result<UserDTO>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val item = dataSnapshot.getValue(UserDTO::class.java) ?: UserDTO()
                this@callbackFlow.sendBlocking(Result.success(item))
            }
        }
        database.child("users").child(id)
            .addValueEventListener(postListener)

        awaitClose {
            database.child("users").child(id)
                .removeEventListener(postListener)
        }
    }
}