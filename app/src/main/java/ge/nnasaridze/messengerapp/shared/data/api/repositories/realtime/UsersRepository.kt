package ge.nnasaridze.messengerapp.shared.data.api.repositories.realtime

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.data.api.dtos.UserDTO
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.utils.DATABASE_URL

interface UsersRepository {
    fun createUser(
        user: UserEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    )

    fun updateUser(
        user: UserEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    )

    fun getAndListenUser(
        id: String,
        handler: (isSuccessful: Boolean, user: UserEntity) -> Unit,
    )

    fun getUsers(
        from: Int = 0,
        to: Int = 0,
        nameQuery: String = "",
        handler: (isSuccessful: Boolean, user: UserEntity) -> Unit,
    )

    fun cancelSubscriptions()
}

class DefaultUsersRepository : UsersRepository {


    private val database = Firebase.database(DATABASE_URL).reference
    private val subscriptions = mutableListOf<() -> Unit>()

    override fun createUser(user: UserEntity, handler: (isSuccessful: Boolean) -> Unit) {
        val userDTO = UserDTO(user.nickname, user.nickname, hashMapOf())
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

    override fun getAndListenUser(
        id: String,
        handler: (isSuccessful: Boolean, user: UserEntity) -> Unit,
    ) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val item = dataSnapshot.getValue(UserDTO::class.java)?.toEntity(id)
                if (item != null) {
                    handler(true, item)
                } else handler(false, UserEntity("", "", ""))
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        val ref = database.child("users").child(id)
        ref.addValueEventListener(listener)
        subscriptions.add { ref.removeEventListener(listener) }
    }

    override fun getUsers(
        from: Int,
        to: Int,
        nameQuery: String,
        handler: (isSuccessful: Boolean, user: UserEntity) -> Unit,
    ) {
        var count = 0

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val userID = ds.key ?: continue
                    val userDTO = ds.getValue(UserDTO::class.java) ?: continue
                    if (userDTO.nickname == null) continue
                    if (nameQuery in userDTO.nickname && count >= from) {
                        handler(true, userDTO.toEntity(userID))
                    }
                    count++
                }
                if (count == 0)
                    handler(false, UserEntity("", "", ""))
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        val ref = database.child("users")
        if (to != 0)
            ref.limitToFirst(to).addListenerForSingleValueEvent(listener)
        else ref.addListenerForSingleValueEvent(listener)
    }

    override fun cancelSubscriptions() {
        for (remover in subscriptions)
            remover.invoke()
    }
}