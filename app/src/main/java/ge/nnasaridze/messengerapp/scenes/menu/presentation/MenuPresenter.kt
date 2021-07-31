package ge.nnasaridze.messengerapp.scenes.menu.presentation

import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.DefaultGetChatsUsecase
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.DefaultSignoutUsecase
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.GetChatsUsecase
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.SignoutUsecase
import ge.nnasaridze.messengerapp.shared.repositories.chats.ChatDTO

class MenuPresenterImpl(private val view: MenuView) : MenuPresenter {

    private val getChatsUsecase: GetChatsUsecase
    private val signoutUsecase: SignoutUsecase

    private val data = mutableMapOf<String, ChatDTO>()
    private var query = ""

    init {
        getChatsUsecase = DefaultGetChatsUsecase()
        signoutUsecase  = DefaultSignoutUsecase()

        getChatsUsecase.execute(::chatUpdateHandler)
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
        view.gotoLogin()
    }

    override fun imagePressed() {
        view.pickImage()
    }

    private fun chatUpdateHandler(chatID: String, chat: ChatDTO) {
        data[chatID] = chat
        updateData()
    }

    private fun updateData() {
        val filteredData = mutableListOf<ChatDTO>()
        for (chat in data.values)
            if (chat.user?.nickname?.contains(query) == true)
                filteredData.add(chat)
        filteredData.sortByDescending { it.lastMessage?.timestamp }
        view.updateConversations(filteredData)
    }
}