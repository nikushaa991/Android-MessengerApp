package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.repositories.chats.DefaultChatsRepository
import ge.nnasaridze.messengerapp.shared.utils.LAZY_LOADING_AMOUNT

interface GetChatsUsecase {
    fun execute(chatsAmountToLoad: Int, newChatHandler: (chatID: String) -> Unit)
}

class DefaultGetChatsUsecase : GetChatsUsecase {


    private val userID = DefaultAuthenticationRepository().getID()
    private val chats = DefaultChatsRepository()

    override fun execute(chatsAmountToLoad: Int, newChatHandler: (chatID: String) -> Unit) {
        chats.getAndSubscribeChatIDs(userID,
            chatsAmountToLoad - LAZY_LOADING_AMOUNT,
            chatsAmountToLoad,
            newChatHandler)
    }
}