package ge.nnasaridze.messengerapp.scenes.menu.data.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ge.nnasaridze.messengerapp.shared.repositories.Repository
import ge.nnasaridze.messengerapp.shared.repositories.chats.ChatDTO

class ChatsRepository {


    private val repo = Repository.getInstance()

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
        repo.getChats(id).addValueEventListener(postListener)
    }

    fun fetch(chatID: String, handler: (chatID: String, data: ChatDTO) -> Unit) {
        repo.getChat(chatID).get()
            .addOnSuccessListener { handler(chatID, it.value as ChatDTO) }
    }
}