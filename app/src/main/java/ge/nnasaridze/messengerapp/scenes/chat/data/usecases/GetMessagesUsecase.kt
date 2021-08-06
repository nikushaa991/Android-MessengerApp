package ge.nnasaridze.messengerapp.scenes.chat.data.usecases

import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.RecyclerMessageEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.chats.DefaultChatsRepository

interface GetMessagesUsecase {
    fun execute(
        chatID: String,
        newMessageHandler: (message: RecyclerMessageEntity) -> Unit,
    )
}

class DefaultGetMessagesUsecase : GetMessagesUsecase {


    private val userID = DefaultAuthenticationRepository().getID()
    private val chats = DefaultChatsRepository()

    override fun execute(
        chatID: String,
        newMessageHandler: (message: RecyclerMessageEntity) -> Unit,
    ) {
        chats.getAndSubscribeMessages(userID) { message ->
            val entity = RecyclerMessageEntity(
                message.text,
                message.timestamp,
                chatID == message.senderID)

            newMessageHandler(entity)

        }
    }
}