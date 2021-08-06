package ge.nnasaridze.messengerapp.shared.data.repositories.users

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.dtos.UserDTO

interface UsersRepository {
    fun createUser(
        user: UserEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    )

    fun updateUser(
        user: UserEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    )

    fun subscribeUser(
        id: String,
        handler: (UserEntity) -> Unit,
    )
}

class DefaultUsersRepository : UsersRepository {


    private val database = Firebase.database.reference


    override fun createUser(user: UserEntity, handler: (isSuccessful: Boolean) -> Unit) {
        val userDTO = UserDTO(user.nickname, user.nickname, listOf())
        database.child("users").child(user.userID).setValue(userDTO)
            .addOnCompleteListener { handler(it.isSuccessful) }
    }


    override fun updateUser(user: UserEntity, handler: (isSuccessful: Boolean) -> Unit) {

        val childUpdates = hashMapOf<String, Any>(
            "/users/${user.userID}/nickname" to user.nickname,
            "/users/${user.userID}/profession" to user.profession
        )

        database.updateChildren(childUpdates)
            .addOnCompleteListener { task ->
                handler(task.isSuccessful)
            }
    }

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