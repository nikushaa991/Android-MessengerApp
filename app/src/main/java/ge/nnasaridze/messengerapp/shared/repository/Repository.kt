package ge.nnasaridze.messengerapp.shared.repository

import com.google.firebase.database.DatabaseReference
import ge.nnasaridze.messengerapp.shared.repository.auth.Authenticator
import ge.nnasaridze.messengerapp.shared.repository.realtimedb.Database

interface Repository {
    fun getID(): String
    fun isAuthenticated(): Boolean
    fun authenticate(nickname: String, password: String, handler: (isSuccessful: Boolean) -> Unit)
    fun register(
        nickname: String,
        password: String,
        profession: String,
        handler: (isSuccessful: Boolean) -> Unit
    )

    fun getUser(id: String): DatabaseReference
    fun getChats(id: String): DatabaseReference
    fun getChat(chatID: String): DatabaseReference
    fun getMessages(chatID: String): DatabaseReference

    companion object {
        private var instance = RepositoryImpl()

        fun getInstance(): Repository {
            return instance
        }
    }
}

class RepositoryImpl : Repository {
    private val auth = Authenticator()
    private val db = Database()

    override fun getID(): String {
        return auth.getID()
    }

    override fun isAuthenticated(): Boolean {
        return auth.isAuthenticated()
    }

    override fun authenticate(
        nickname: String,
        password: String,
        handler: (isSuccessful: Boolean) -> Unit
    ) {
        auth.authenticate(nickname, password, handler)
    }

    override fun register(
        nickname: String,
        password: String,
        profession: String,
        handler: (isSuccessful: Boolean) -> Unit
    ) {
        auth.register(nickname, password) { isSuccessful ->
            if (isSuccessful) {
                db.register(getID(), nickname, profession) { isSuccessfulDB ->
                    handler(isSuccessfulDB)
                }
            } else handler(isSuccessful)
        }
    }

    override fun getUser(id: String): DatabaseReference {
        return db.getUser(id)
    }

    override fun getChats(id: String): DatabaseReference {
        return db.getChats(id)
    }

    override fun getChat(chatID: String): DatabaseReference {
        return db.getChat(chatID)
    }

    override fun getMessages(chatID: String): DatabaseReference {
        return db.getMessages(chatID)
    }
}