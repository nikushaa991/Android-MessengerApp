package ge.nnasaridze.messengerapp.scenes.search.data.usecases

import ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.realtime.DefaultChatsRepository

interface CreateChatUsecase {
    fun execute(
        recipientID: String,
        onSuccessHandler: (chatID: String) -> Unit,
        errorHandler: (text: String) -> Unit,
    )
}

class DefaultCreateChatUsecase : CreateChatUsecase {

    private val currID = DefaultAuthenticationRepository().getID()
    private val chatsRepo = DefaultChatsRepository()

    override fun execute(
        recipientID: String,
        onSuccessHandler: (chatID: String) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        chatsRepo.addChat(currID, recipientID) { isSuccessful, chatID ->
            if (!isSuccessful) {
                errorHandler("Chat creation failed")
                return@addChat
            }
            onSuccessHandler(chatID)
        }
    }
}