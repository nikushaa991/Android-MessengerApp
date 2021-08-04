package ge.nnasaridze.messengerapp.shared.repositories.users

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.repositories.dtos.UserDTO

interface UsersRepository {
    fun subscribeUser(
        id: String,
        handler: (UserEntity) -> Unit
    )
}

class DefaultUsersRepository : UsersRepository {


    private val database = Firebase.database.reference
    override fun subscribeUser(id: String, handler: (UserEntity) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val item = dataSnapshot.getValue(UserDTO::class.java)?.toEntity(id)
                if (item != null) {
                    handler(item)
                }
            }
        }
        database.child("users").child(id)
            .addValueEventListener(listener)
    }
}