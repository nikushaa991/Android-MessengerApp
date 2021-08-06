package ge.nnasaridze.messengerapp.shared.data.repositories.chats

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.data.entities.ChatEntity
import ge.nnasaridze.messengerapp.shared.data.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.dtos.ChatDTO
import ge.nnasaridze.messengerapp.shared.data.repositories.dtos.MessageDTO

interface ChatsRepository {
    fun getAndSubscribeChatIDs(
        id: String,
        newChatIDHandler: (String) -> Unit,
    )

    fun subscribeChat(chatID: String, handler: (ChatEntity) -> Unit)

    fun getAndSubscribeMessages(
        chatID: String,
        newMessageHandler: (MessageEntity) -> Unit,
    )

    fun subscribeLastMessage(
        chatID: String,
        handler: (MessageEntity) -> Unit,
    )

    fun addMessage(
        chatID: String,
        message: MessageEntity,
        handler: (Boolean) -> Unit,
    )
}

class DefaultChatsRepository : ChatsRepository {


    private val database = Firebase.database.reference

    override fun getAndSubscribeChatIDs(
        id: String,
        newChatIDHandler: (String) -> Unit,
    ) {
        val listener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val item = dataSnapshot.getValue(String::class.java)
                if (item != null) {
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
        database.child("users").child(id).child("chatIDs")
            .addChildEventListener(listener)
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
        newMessageHandler: (MessageEntity) -> Unit,
    ) {
        val listener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val item =
                    dataSnapshot.getValue(MessageDTO::class.java)?.toEntity()
                if (item != null) {
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
        database.child("messages").child(chatID)
            .addChildEventListener(listener)
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
                            val message = ds.getValue(MessageDTO::class.java)?.toEntity()
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

    override fun addMessage(chatID: String, message: MessageEntity, handler: (Boolean) -> Unit) {
        val messageID = database.child("messages").child(chatID).push().key
        if (messageID == null) {
            handler(false)
            return
        }

        val messageValues = MessageDTO(message.senderID, message.text, message.timestamp).toMap()

        val childUpdates = hashMapOf(
            "/messages/$chatID/$messageID" to messageValues,
            "/chats/$chatID/lastMessageID" to messageID
        )

        database.updateChildren(childUpdates)
            .addOnCompleteListener { task ->
                handler(task.isSuccessful)
            }
    }
}

