package ge.nnasaridze.messengerapp.scenes.chat.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.chats.DefaultChatsRepository

interface SendMessageUsecase {
    fun execute(chatID: String, text: String, handler: (isSuccessful: Boolean) -> Unit)
}

class DefaultSendMessageUsecase : SendMessageUsecase {

    private val repo = DefaultChatsRepository()
    private val userID = DefaultAuthenticationRepository().getID()

    override fun execute(chatID: String, text: String, handler: (isSuccessful: Boolean) -> Unit) {
        val message = MessageEntity(userID, text, System.currentTimeMillis())
        repo.addMessage(chatID, message, handler)
    }
}