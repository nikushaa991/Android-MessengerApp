package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.entities.ChatEntity
import ge.nnasaridze.messengerapp.shared.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.repositories.dtos.ChatDTO
import ge.nnasaridze.messengerapp.shared.repositories.chats.DefaultChatsRepository
import kotlinx.coroutines.*

interface GetChatsUsecase {
    fun execute(
        newChatHandler: (chatID: String) -> Unit,
        updateChatHandler: (chat: ChatEntity) -> Unit,
    )
}

class DefaultGetChatsUsecase : GetChatsUsecase {


    private val auth = DefaultAuthenticationRepository()
    private val chats = DefaultChatsRepository()

    override fun execute(
        newChatHandler: (chatID: String) -> Unit,
        updateChatHandler: (chat: ChatEntity) -> Unit,
    ) {
        val userID = auth.getID()

        chats.getAndSubscribeChatIDs(userID) { chatID: String ->
            newChatHandler(chatID)
            chats.subscribeChat(chatID, updateChatHandler)
        }
    }
}