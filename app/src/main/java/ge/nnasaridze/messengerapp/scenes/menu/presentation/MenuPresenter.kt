package ge.nnasaridze.messengerapp.scenes.menu.presentation

import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.GetChatsUsecase
import ge.nnasaridze.messengerapp.shared.repository.realtimedb.entities.ChatEntity

class MenuPresenterImpl(private val view: MenuView) : MenuPresenter {
    private val data = mutableMapOf<String, ChatEntity>()
    private var query = ""

    init {
        GetChatsUsecase().execute(::chatUpdateHandler)
    }

    override fun fabPressed() {
        view.gotoSearch()
    }

    override fun homePressed() {
        view.setConversationsFragment()
    }

    override fun chatPressed(position: Int) {
        view.gotoChat()
    }

    override fun searchEdited(text: String) {
        query = text
        updateData()
    }

    override fun settingsPressed() {
        view.setSettingsFragment()
    }

    override fun updatePressed() {
        TODO("Not yet implemented")
    }

    override fun signoutPressed() {
        SignoutUsecase().execute()
        view.gotoLogin()
    }

    override fun imagePressed() {
        view.pickImage()
    }

    private fun chatUpdateHandler(chatID: String, chat: ChatEntity) {
        data[chatID] = chat
        updateData()
    }

    private fun updateData() {
        val filteredData = mutableListOf<ChatEntity>()
        for (chat in data.values)
            if (chat.user?.nickname?.contains(query) == true)
                filteredData.add(chat)
        filteredData.sortByDescending { it.lastMessage?.timestamp }
        view.updateConversations(filteredData)
    }
}