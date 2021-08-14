package ge.nnasaridze.messengerapp.shared.data.api.repositories.realtime

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.data.api.dtos.ChatDTO
import ge.nnasaridze.messengerapp.shared.data.api.dtos.MessageDTO
import ge.nnasaridze.messengerapp.shared.data.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.utils.DATABASE_URL

interface ChatsRepository {
    fun getAndSubscribeChatIDs(
        id: String,
        handler: (isSuccessful: Boolean, chatID: String) -> Unit,
    )

    fun addChat(
        userID1: String,
        userID2: String,
        handler: (isSuccessful: Boolean, chatID: String) -> Unit,
    )

    fun getChatMembers(
        chatID: String,
        handler: (isSuccessful: Boolean, members: List<String>) -> Unit,
    )

    fun getAndListenChatLastMessageID(
        chatID: String,
        handler: (isSuccessful: Boolean, messageID: String) -> Unit,
    )

    fun getAndSubscribeMessages(
        chatID: String,
        handler: (isSuccessful: Boolean, message: MessageEntity) -> Unit,
    )

    fun addMessage(
        chatID: String,
        message: MessageEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    )

    fun getMessage(
        chatID: String,
        messageID: String,
        handler: (isSuccessful: Boolean, message: MessageEntity) -> Unit,
    )

    fun cancelSubscriptions()
}

class DefaultChatsRepository : ChatsRepository {


    private val database = Firebase.database(DATABASE_URL).reference
    private val subscriptions = mutableListOf<() -> Unit>()

    override fun getAndSubscribeChatIDs(
        id: String,
        handler: (isSuccessful: Boolean, chatID: String) -> Unit,
    ) {
        val listener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val chatID = dataSnapshot.getValue(String::class.java)
                if (chatID != null) {
                    handler(true, chatID)
                } else handler(false, "")
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
        ref.addChildEventListener(listener)
        subscriptions.add { ref.removeEventListener(listener) }
    }


    override fun addChat(
        userID1: String,
        userID2: String,
        handler: (isSuccessful: Boolean, chatID: String) -> Unit,
    ) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatID = snapshot.getValue(String::class.java)
                if (chatID == null) {
                    createChat(userID1, userID2, handler)
                } else handler(true, chatID)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        database.child("users/$userID1/chatIDs/$userID2")
            .addListenerForSingleValueEvent(listener)
    }

    override fun getChatMembers(
        chatID: String,
        handler: (isSuccessful: Boolean, members: List<String>) -> Unit,
    ) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chat = snapshot.getValue(ChatDTO::class.java)
                if (chat?.userIDs != null) {
                    handler(true, chat.userIDs.values.toList())
                } else handler(false, listOf())
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        database.child("chats").child(chatID)
            .addListenerForSingleValueEvent(listener)
    }

    override fun getAndListenChatLastMessageID(
        chatID: String,
        handler: (isSuccessful: Boolean, messageID: String) -> Unit,
    ) {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messageID = dataSnapshot.getValue(String::class.java)
                if (messageID != null) {
                    handler(true, messageID)
                } else handler(false, "")

            }
        }
        val ref = database.child("chats").child(chatID).child("lastMessageID")
        ref.addValueEventListener(listener)
        subscriptions.add { ref.removeEventListener(listener) }
    }

    override fun getAndSubscribeMessages(
        chatID: String,
        handler: (isSuccessful: Boolean, message: MessageEntity) -> Unit,
    ) {
        val listener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val item =
                    dataSnapshot.getValue(MessageDTO::class.java)?.toEntity()
                if (item != null) {
                    handler(true, item)
                } else handler(false, MessageEntity("", "", 0))
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
        ref.addChildEventListener(listener)
        subscriptions.add { ref.removeEventListener(listener) }
    }


    override fun addMessage(
        chatID: String,
        message: MessageEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    ) {
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

    override fun getMessage(
        chatID: String,
        messageID: String,
        handler: (isSuccessful: Boolean, message: MessageEntity) -> Unit,
    ) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val message = snapshot.getValue(MessageDTO::class.java)?.toEntity()
                if (message != null) {
                    handler(true, message)
                } else handler(false, MessageEntity("", "", 0))
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        database.child("messages").child(chatID).child(messageID)
            .addListenerForSingleValueEvent(listener)
    }

    override fun cancelSubscriptions() {
        for (remover in subscriptions)
            remover.invoke()
    }

    private fun createChat(
        userID1: String,
        userID2: String,
        handler: (isSuccessful: Boolean, chatID: String) -> Unit,
    ) {
        val chatID = database.child("chats").push().key
        if (chatID == null) {
            handler(false, "")
            return
        }
        val messageID = database.child("messages").child(chatID).push().key

        if (messageID == null) {
            handler(false, "")
            return
        }
        val userIDs = hashMapOf("user1" to userID1, "user2" to userID2)
        val chatValues = ChatDTO(userIDs, messageID).toMap()
        val messageValues =
            MessageDTO("SYSTEM", "Start of conversation", System.currentTimeMillis()).toMap()

        val childUpdates = hashMapOf(
            "/chats/$chatID" to chatValues,
            "/messages/$chatID/$messageID" to messageValues,
            "users/$userID1/chatIDs/$userID2" to chatID,
            "users/$userID2/chatIDs/$userID1" to chatID,
        )

        database.updateChildren(childUpdates)
            .addOnCompleteListener { task ->
                handler(task.isSuccessful, chatID)
            }
    }
}

