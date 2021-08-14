package ge.nnasaridze.messengerapp.scenes.chat.data.usecases

import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.RecyclerMessageEntity
import ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.realtime.DefaultChatsRepository

interface SubscribeMessagesUsecase {
    fun execute(
        chatID: String,
        newMessageHandler: (message: RecyclerMessageEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    )

    fun destroy()
}

class DefaultSubscribeMessagesUsecase : SubscribeMessagesUsecase {

    private val currentID = DefaultAuthenticationRepository().getID()
    private val chats = DefaultChatsRepository()

    override fun execute(
        chatID: String,
        newMessageHandler: (message: RecyclerMessageEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        chats.getAndSubscribeMessages(chatID) { isSuccessful, message ->
            if (!isSuccessful) {
                errorHandler("Fetching message failed")
                return@getAndSubscribeMessages
            }

            val entity = RecyclerMessageEntity(
                message.text,
                message.timestamp,
                currentID == message.senderID)
            newMessageHandler(entity)
        }
    }

    override fun destroy() {
        chats.cancelSubscriptions()
    }
}