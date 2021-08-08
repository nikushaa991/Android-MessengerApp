package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import android.net.Uri
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.recycler.RecyclerChatEntity
import ge.nnasaridze.messengerapp.shared.data.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.chats.DefaultChatsRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.pictures.DefaultPicturesRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.users.DefaultUsersRepository

interface SubscribeChatsUsecase {
    fun execute(
        newChatHandler: (chat: RecyclerChatEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    )

    fun destroy()
}

class DefaultSubscribeChatsUsecase : SubscribeChatsUsecase {

    private val currentId = DefaultAuthenticationRepository().getID()
    private val usersRepo = DefaultUsersRepository()
    private val chatsRepo = DefaultChatsRepository()
    private val imagesRepo = DefaultPicturesRepository()

    private val messageHandlerVersions = mutableMapOf<String, Int>()
    private val usersSubTokens = mutableSetOf<Int>()
    private val chatsSubTokens = mutableSetOf<Int>()
    private var isDestroyed = false

    private lateinit var newChatHandler: (chat: RecyclerChatEntity) -> Unit
    private lateinit var errorHandler: (text: String) -> Unit

    override fun execute(
        newChatHandler: (chat: RecyclerChatEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        this.newChatHandler = newChatHandler
        this.errorHandler = errorHandler

        chatsRepo.getAndSubscribeChatIDs(currentId) { isSuccessful, chatID, subToken ->
            if (isDestroyed)
                return@getAndSubscribeChatIDs
            chatsSubTokens.add(subToken)
            if (!isSuccessful) {
                errorHandler("Fetching chat failed")
                return@getAndSubscribeChatIDs
            }
            getChatUserID(chatID)
        }
    }

    override fun destroy() {
        isDestroyed = true
        for (token in usersSubTokens)
            usersRepo.cancelSubscription(token)
        for (token in chatsSubTokens)
            chatsRepo.cancelSubscription(token)
    }

    private fun getChatUserID(chatID: String) {
        chatsRepo.getChatMembers(chatID) { isSuccessful, members ->
            if (isDestroyed)
                return@getChatMembers
            if (!isSuccessful) {
                errorHandler("Fetching chat user failed")
                return@getChatMembers
            }
            val userID = if (currentId != members[0]) members[0] else members[1]
            getChatUser(chatID, userID)
        }
    }

    private fun getChatUser(chatID: String, userID: String) {
        var version = -1
        usersRepo.getAndListenUser(userID) { isSuccessful, user, subToken ->
            if (isDestroyed)
                return@getAndListenUser
            usersSubTokens.add(subToken)
            if (!isSuccessful) {
                errorHandler("Fetching chat user failed")
                return@getAndListenUser
            }
            messageHandlerVersions[chatID] = ++version
            getLastMessageID(version, chatID, user)
        }
    }

    private fun getLastMessageID(version: Int, chatID: String, user: UserEntity) {
        chatsRepo.getAndListenChatLastMessageID(chatID) { isSuccessful, messageID, subToken ->
            if (isDestroyed)
                return@getAndListenChatLastMessageID
            chatsSubTokens.add(subToken)
            if (!isSuccessful) {
                errorHandler("Fetching chat user failed")
                return@getAndListenChatLastMessageID
            }
            if (version < messageHandlerVersions[chatID] ?: version + 1) {
                chatsRepo.cancelSubscription(subToken)
            }
            getLastMessage(chatID, messageID, user)
        }
    }

    private fun getLastMessage(chatID: String, messageID: String, user: UserEntity) {
        chatsRepo.getMessage(messageID) { isSuccessful, message ->
            if (isDestroyed)
                return@getMessage
            if (!isSuccessful) {
                errorHandler("Fetching message failed")
                return@getMessage
            }
            constructChat(chatID, user, message)
        }
    }

    private fun constructChat(chatID: String, user: UserEntity, message: MessageEntity) {
        val imageUri = imagesRepo.getPictureURL(user.userID) ?: Uri.EMPTY
        if (imageUri == Uri.EMPTY) {
            errorHandler("Fetching image failed")
        }
        newChatHandler(RecyclerChatEntity(chatID, user, message, imageUri))
    }

}