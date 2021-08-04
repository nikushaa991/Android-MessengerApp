package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.repositories.chats.DefaultChatsRepository

interface GetLastMessageUsecase {
    fun execute(chatID: String, handler: (message: MessageEntity) -> Unit)
}

class DefaultGetLastMessageUsecase : GetLastMessageUsecase {
    private val repo = DefaultChatsRepository()
    override fun execute(chatID: String, handler: (user: MessageEntity) -> Unit) {
        repo.subscribeLastMessage(chatID, handler)
    }
}