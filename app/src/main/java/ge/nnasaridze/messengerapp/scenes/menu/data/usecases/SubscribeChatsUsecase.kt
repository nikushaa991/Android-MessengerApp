package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.recycler.RecyclerChatEntity
import ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.chats.DefaultChatsRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.pictures.DefaultPicturesRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.users.DefaultUsersRepository
import ge.nnasaridze.messengerapp.shared.data.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

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

    private lateinit var newChatHandler: (chat: RecyclerChatEntity) -> Unit
    private lateinit var errorHandler: (text: String) -> Unit
    private val localRepos = mutableSetOf<DefaultChatsRepository>()

    override fun execute(
        newChatHandler: (chat: RecyclerChatEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        this.newChatHandler = newChatHandler
        this.errorHandler = errorHandler

        chatsRepo.getAndSubscribeChatIDs(currentId) { isSuccessful, chatID ->
            if (!isSuccessful) {
                errorHandler("Fetching chat failed")
                return@getAndSubscribeChatIDs
            }
            getChatUserID(chatID)
        }
    }

    override fun destroy() {
        usersRepo.cancelSubscriptions()
        chatsRepo.cancelSubscriptions()
        for (repo in localRepos)
            repo.cancelSubscriptions()
    }

    private fun getChatUserID(chatID: String) {
        chatsRepo.getChatMembers(chatID) { isSuccessful, members ->
            if (!isSuccessful) {
                errorHandler("Fetching chat user failed")
                return@getChatMembers
            }
            val userID = if (currentId != members[0]) members[0] else members[1]
            getChatUser(chatID, userID)
        }
    }

    private fun getChatUser(chatID: String, userID: String) {
        val localRepo = DefaultChatsRepository()
        localRepos.add(localRepo)
        usersRepo.getAndListenUser(userID) { isSuccessful, user ->
            if (!isSuccessful) {
                errorHandler("Fetching chat user failed")
                return@getAndListenUser
            }
            localRepo.cancelSubscriptions()
            getLastMessageID(chatID, user, localRepo)
        }
    }

    private fun getLastMessageID(chatID: String, user: UserEntity, localRepo: DefaultChatsRepository) {
        localRepo.getAndListenChatLastMessageID(chatID) { isSuccessful, messageID ->
            if (!isSuccessful) {
                errorHandler("Fetching chat user failed")
                return@getAndListenChatLastMessageID
            }
            getLastMessage(chatID, messageID, user)
        }
    }

    private fun getLastMessage(chatID: String, messageID: String, user: UserEntity) {
        chatsRepo.getMessage(chatID, messageID) { isSuccessful, message ->
            if (!isSuccessful) {
                errorHandler("Fetching message failed")
                return@getMessage
            }
            constructChat(chatID, user, message)
        }
    }

    private fun constructChat(chatID: String, user: UserEntity, message: MessageEntity) {
        imagesRepo.getPictureURL(user.userID) { _, uri ->
            newChatHandler(RecyclerChatEntity(chatID, user, message, uri))
        }
    }
}