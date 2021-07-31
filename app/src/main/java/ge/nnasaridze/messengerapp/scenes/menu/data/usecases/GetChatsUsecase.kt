package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.scenes.menu.data.repositories.ChatsRepository
import ge.nnasaridze.messengerapp.shared.repositories.Repository
import ge.nnasaridze.messengerapp.shared.repositories.chats.ChatDTO

interface GetChatsUsecase {
    fun execute(handler: (chatID: String, data: ChatDTO) -> Unit)
}

class DefaultGetChatsUsecase : GetChatsUsecase {


    private val repo = ChatsRepository()

    override fun execute(handler: (chatID: String, data: ChatDTO) -> Unit) {
        val userID = Repository.getInstance().getID()
        repo.subscribe(userID) { chatIDs ->
            for (chatID in chatIDs)
                repo.fetch(chatID, handler)
        }
    }
}