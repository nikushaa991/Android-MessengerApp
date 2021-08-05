package ge.nnasaridze.messengerapp.scenes.chat.data.usecases

import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.RecyclerMessageEntity
import ge.nnasaridze.messengerapp.shared.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.repositories.chats.DefaultChatsRepository
import ge.nnasaridze.messengerapp.shared.utils.LAZY_LOADING_AMOUNT

interface GetMessagesUsecase {
    fun execute(
        chatID: String,
        messagesAmountToLoad: Int,
        newMessageHandler: (message: RecyclerMessageEntity) -> Unit,
    )
}

class DefaultGetMessagesUsecase : GetMessagesUsecase {


    private val userID = DefaultAuthenticationRepository().getID()
    private val chats = DefaultChatsRepository()

    override fun execute(
        chatID: String,
        messagesAmountToLoad: Int,
        newMessageHandler: (message: RecyclerMessageEntity) -> Unit,
    ) {
        chats.getAndSubscribeMessages(userID,
            messagesAmountToLoad - LAZY_LOADING_AMOUNT,
            messagesAmountToLoad) { message ->
            val entity =
                RecyclerMessageEntity(message.text, message.timestamp, chatID == message.senderID)
            newMessageHandler(entity)

        }
    }
}