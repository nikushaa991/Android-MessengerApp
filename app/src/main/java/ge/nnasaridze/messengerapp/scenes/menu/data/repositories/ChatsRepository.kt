package ge.nnasaridze.messengerapp.scenes.menu.data.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ge.nnasaridze.messengerapp.shared.repository.Repository
import ge.nnasaridze.messengerapp.shared.repository.realtimedb.entities.ChatEntity

class ChatsRepository {
    fun subscribe(
        id: String,
        handler: (data: List<String>) -> Unit
    ) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                handler(dataSnapshot.value as List<String>)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        Repository.getInstance().getChats(id).addValueEventListener(postListener)
    }

    fun fetch(chatID: String, handler: (chatID: String, data: ChatEntity) -> Unit) {
        Repository.getInstance().getChat(chatID).get()
            .addOnSuccessListener { handler(chatID, it.value as ChatEntity) }
    }
}