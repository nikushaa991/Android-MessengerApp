package ge.nnasaridze.messengerapp.shared.data.repositories.chats

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.data.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.dtos.ChatDTO
import ge.nnasaridze.messengerapp.shared.data.repositories.dtos.MessageDTO

interface ChatsRepository {
    fun getAndSubscribeChatIDs(
        id: String,
        handler: (isSuccessful: Boolean, chatID: String, subToken: Int) -> Unit,
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
        handler: (isSuccessful: Boolean, messageID: String, subToken: Int) -> Unit,
    )

    fun getAndSubscribeMessages(
        chatID: String,
        handler: (isSuccessful: Boolean, message: MessageEntity, subToken: Int) -> Unit,
    )

    fun addMessage(
        chatID: String,
        message: MessageEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    )

    fun getMessage(
        messageID: String,
        handler: (isSuccessful: Boolean, message: MessageEntity) -> Unit,
    )

    fun cancelSubscription(subToken: Int)
}

class DefaultChatsRepository : ChatsRepository {


    private val database = Firebase.database.reference
    private val subscriptions = mutableMapOf<Int, () -> Unit>()
    private var subscriptionNextFreeToken = 0

    override fun getAndSubscribeChatIDs(
        id: String,
        handler: (isSuccessful: Boolean, chatID: String, subToken: Int) -> Unit,
    ) {
        val subToken = generateSubToken()

        val listener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val chatID = dataSnapshot.getValue(String::class.java)
                if (chatID != null) {
                    handler(true, chatID, subToken)
                } else handler(false, "", subToken)
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
        subscriptions[subToken] = { ref.removeEventListener(listener) }
    }


    override fun addChat(
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
        val user1Key = database.child("users").child(userID1).child("chats").push().key
        val user2Key = database.child("users").child(userID2).child("chats").push().key

        if (messageID == null || user1Key == null || user2Key == null) {
            handler(false, "")
            return
        }

        val chatValues = ChatDTO(listOf(userID1, userID2), messageID).toMap()
        val messageValues =
            MessageDTO("SYSTEM", "Start of conversation", System.currentTimeMillis()).toMap()

        val childUpdates = hashMapOf(
            "/chats/$chatID" to chatValues,
            "/messages/$chatID/$messageID" to messageValues,
            "users/$userID1/chats/$user1Key" to chatID,
            "users/$userID2/chats/$user2Key" to chatID,
        )

        database.updateChildren(childUpdates)
            .addOnCompleteListener { task ->
                handler(task.isSuccessful, chatID)
            }
    }

    override fun getChatMembers(
        chatID: String,
        handler: (isSuccessful: Boolean, members: List<String>) -> Unit,
    ) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chat = snapshot.getValue(ChatDTO::class.java)
                if (chat != null) {
                    handler(true, chat.userIDs)
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
        handler: (isSuccessful: Boolean, messageID: String, subToken: Int) -> Unit,
    ) {
        val subToken = generateSubToken()

        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messageID = dataSnapshot.getValue(String::class.java)
                if (messageID != null) {
                    handler(true, messageID, subToken)
                } else handler(false, "", subToken)

            }
        }
        val ref = database.child("chats").child(chatID).child("lastMessage")
        ref.addValueEventListener(listener)
        subscriptions[subToken] = { ref.removeEventListener(listener) }
    }

    override fun getAndSubscribeMessages(
        chatID: String,
        handler: (isSuccessful: Boolean, message: MessageEntity, subToken: Int) -> Unit,
    ) {
        val subToken = generateSubToken()

        val listener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val item =
                    dataSnapshot.getValue(MessageDTO::class.java)?.toEntity()
                if (item != null) {
                    handler(true, item, subToken)
                } else handler(false, MessageEntity("", "", 0), subToken)
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
        subscriptions[subToken] = { ref.removeEventListener(listener) }
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

        database.child("messages").child(messageID)
            .addListenerForSingleValueEvent(listener)
    }

    override fun cancelSubscription(subToken: Int) {
        if (subToken in subscriptions) {
            val remover = subscriptions.remove(subToken)
            remover?.invoke()
        }
    }


    private fun generateSubToken(): Int {
        return subscriptionNextFreeToken++
    }
}

