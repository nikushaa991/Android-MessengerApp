package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.scenes.menu.data.repositories.ChatsRepository
import ge.nnasaridze.messengerapp.shared.repository.Repository
import ge.nnasaridze.messengerapp.shared.repository.realtimedb.entities.ChatEntity

class GetChatsUsecase {
    fun execute(handler: (chatID: String, data: ChatEntity) -> Unit) {
        val repo = ChatsRepository()
        val userID = Repository.getInstance().getID()
        repo.subscribe(userID) { chatIDs ->
            for (chatID in chatIDs)
                repo.fetch(chatID, handler)
        }
    }
}