package ge.nnasaridze.messengerapp.scenes.menu.presentation

import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.DefaultGetChatsUsecase
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.DefaultSignoutUsecase
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.GetChatsUsecase
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.SignoutUsecase
import ge.nnasaridze.messengerapp.shared.entities.ChatEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MenuPresenterImpl(private val view: MenuView) : MenuPresenter, CoroutineScope {
    override val coroutineContext = Job() + Dispatchers.Default

    private val getChatsUsecase: GetChatsUsecase
    private val signoutUsecase: SignoutUsecase

    private val data = mutableMapOf<String, ChatEntity>()
    private var query = ""

    init {
        getChatsUsecase = DefaultGetChatsUsecase()
        signoutUsecase = DefaultSignoutUsecase()
    }

    override fun viewInitialized() {
        getChatsUsecase.execute(
            newChatHandler = ::newChatHandler,
            updateChatHandler = ::updateChatHandler,
        )
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
        signoutUsecase.execute()
        view.gotoLogin()
    }

    override fun imagePressed() {
        view.pickImage()
    }

    private fun newChatHandler(chatID: String) {
        data[chatID] = ChatEntity()
    }

    private fun updateChatHandler(chat: ChatEntity) {
        if (chat.chatID == null) return
        data[chat.chatID] = chat
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