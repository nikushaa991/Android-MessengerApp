package ge.nnasaridze.messengerapp.shared.repositories.chats

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.entities.ChatEntity
import ge.nnasaridze.messengerapp.shared.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.repositories.dtos.ChatDTO
import ge.nnasaridze.messengerapp.shared.repositories.dtos.MessageDTO

interface ChatsRepository {
    fun getAndSubscribeChatIDs(
        id: String,
        start: Int = 0,
        end: Int = 0,
        newChatIDHandler: (String) -> Unit,
    )

    fun subscribeChat(chatID: String, handler: (ChatEntity) -> Unit)

    fun getAndSubscribeMessages(
        chatID: String,
        start: Int,
        end: Int,
        newMessageHandler: (MessageEntity) -> Unit,
    )

    fun subscribeLastMessage(
        chatID: String,
        handler: (MessageEntity) -> Unit,
    )
}

class DefaultChatsRepository : ChatsRepository {


    private val database = Firebase.database.reference

    override fun getAndSubscribeChatIDs(
        id: String,
        start: Int,
        end: Int,
        newChatIDHandler: (String) -> Unit,

        ) {
        val listener = object : ChildEventListener {
            private var count = 0
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val item = dataSnapshot.getValue(String::class.java)
                if (item != null && count >= start) {
                    count++
                    newChatIDHandler(item)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        val ref = database.child("users").child(id).child("chatIDs")

        if (end != 0)
            ref.limitToFirst(end).addChildEventListener(listener)
        else
            ref.addChildEventListener(listener)
    }


    override fun subscribeChat(chatID: String, handler: (ChatEntity) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val item = dataSnapshot.getValue(ChatDTO::class.java)?.toEntity(chatID)
                if (item != null) {
                    handler(item)
                }
            }
        }
        database.child("chats").child(chatID)
            .addValueEventListener(listener)
    }

    override fun getAndSubscribeMessages(
        chatID: String,
        start: Int,
        end: Int,
        newMessageHandler: (MessageEntity) -> Unit,
    ) {
        val listener = object : ChildEventListener {
            private var count = 0
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val item =
                    dataSnapshot.getValue(MessageDTO::class.java)?.toEntity(dataSnapshot.key ?: "")
                if (item != null && count >= start) {
                    count++
                    newMessageHandler(item)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        val ref = database.child("messages").child(chatID)


        if (end != 0)
            ref.limitToFirst(end).addChildEventListener(listener)
        else
            ref.addChildEventListener(listener)
    }

    override fun subscribeLastMessage(chatID: String, handler: (MessageEntity) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messageID = dataSnapshot.getValue(String::class.java)
                if (messageID != null) {
                    database.child("messages").child(chatID).child(messageID).get()
                        .addOnSuccessListener { ds ->
                            val message = ds.getValue(MessageDTO::class.java)?.toEntity(messageID)
                            if (message != null) {
                                handler(message)
                            }
                        }
                }
            }
        }
        database.child("chats").child(chatID).child("lastMessage")
            .addValueEventListener(listener)
    }


}

