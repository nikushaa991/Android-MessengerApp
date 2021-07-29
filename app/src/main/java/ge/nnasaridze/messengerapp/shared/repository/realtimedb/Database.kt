package ge.nnasaridze.messengerapp.shared.repository.realtimedb

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.repository.realtimedb.entities.ChatEntity
import ge.nnasaridze.messengerapp.shared.repository.realtimedb.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.repository.realtimedb.entities.UserEntity
import kotlinx.coroutines.CompletableDeferred

class Database {
    private val database = Firebase.database.reference

    fun getUser(id: String): DatabaseReference {
        return database.child("users").child(id)
    }

    fun getChats(id: String): DatabaseReference {
        return database.child("users").child(id).child("chatIDs")
    }

    fun getChat(chatID: String): DatabaseReference {
        return database.child("chats").child(chatID)
    }

    fun getMessages(chatID: String): DatabaseReference {
        return database.child("messages").child(chatID)
    }

    fun register(
        id: String,
        nickname: String,
        profession: String,
        handler: (isSuccessful: Boolean) -> Unit
    ) {
        val user = UserEntity(nickname, profession, listOf(), 0)
        database.child("users").child(id).setValue(user).addOnCompleteListener { task ->
            handler(task.isSuccessful)
        }
    }
}