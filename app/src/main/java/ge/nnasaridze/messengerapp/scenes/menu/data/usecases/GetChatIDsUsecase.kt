package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.chats.DefaultChatsRepository

interface GetChatIDsUsecase {
    fun execute(newChatHandler: (chatID: String) -> Unit)
}

class DefaultGetChatIDsUsecase : GetChatIDsUsecase {


    private val userID = DefaultAuthenticationRepository().getID()
    private val chats = DefaultChatsRepository()

    override fun execute(newChatHandler: (chatID: String) -> Unit) {
        chats.getAndSubscribeChatIDs(userID, newChatHandler)
    }
}