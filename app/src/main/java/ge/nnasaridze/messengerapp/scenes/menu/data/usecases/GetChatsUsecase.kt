package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.repositories.chats.DefaultChatsRepository

interface GetChatsUsecase {
    fun execute(newChatHandler: (chatID: String) -> Unit)
}

class DefaultGetChatsUsecase : GetChatsUsecase {


    private val auth = DefaultAuthenticationRepository()
    private val chats = DefaultChatsRepository()

    override fun execute(newChatHandler: (chatID: String) -> Unit) {
        val userID = auth.getID()
        chats.getAndSubscribeChatIDs(userID, newChatHandler)
    }
}