package ge.nnasaridze.messengerapp.shared.repositories.chats

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

interface ChatsRepository {
    fun getUsersChatIDs(id: String): Flow<Result<List<String>>>
    fun getChat(chatID: String): Flow<Result<ChatDTO>>
    fun getMessages(chatID: String): Flow<Result<List<MessageDTO>>>
}

class DefaultChatsRepository : ChatsRepository {


    private val database = Firebase.database.reference

    @ExperimentalCoroutinesApi
    override fun getUsersChatIDs(id: String) = callbackFlow<Result<List<String>>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { ds ->
                    ds.value as String
                }
                this@callbackFlow.sendBlocking(Result.success(items))
            }
        }
        database.child("users").child(id).child("chatIDs")
            .addValueEventListener(postListener)

        awaitClose {
            database.child("users").child(id).child("chatIDs")
                .removeEventListener(postListener)
        }
    }


    @ExperimentalCoroutinesApi
    override fun getChat(chatID: String) = callbackFlow<Result<ChatDTO>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val item = dataSnapshot.getValue(ChatDTO::class.java) ?: ChatDTO()

                this@callbackFlow.sendBlocking(Result.success(item))
            }
        }
        database.child("chats").child(chatID)
            .addValueEventListener(postListener)

        awaitClose {
            database.child("chats").child(chatID)
                .removeEventListener(postListener)
        }

    }

    @ExperimentalCoroutinesApi
    override fun getMessages(chatID: String) = callbackFlow<Result<List<MessageDTO>>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { ds ->
                    ds.getValue(MessageDTO::class.java)
                }
                this@callbackFlow.sendBlocking(Result.success(items.filterNotNull()))
            }
        }
        database.child("messages").child(chatID)
            .addValueEventListener(postListener)

        awaitClose {
            database.child("messages").child(chatID)
                .removeEventListener(postListener)
        }
    }
}

