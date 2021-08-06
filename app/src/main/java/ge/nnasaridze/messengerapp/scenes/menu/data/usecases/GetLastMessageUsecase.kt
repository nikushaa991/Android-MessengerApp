package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.chats.DefaultChatsRepository

interface GetLastMessageUsecase {
    fun execute(chatID: String, handler: (message: MessageEntity) -> Unit)
}

class DefaultGetLastMessageUsecase : GetLastMessageUsecase {
    private val repo = DefaultChatsRepository()
    override fun execute(chatID: String, handler: (user: MessageEntity) -> Unit) {
        repo.subscribeLastMessage(chatID, handler)
    }
}