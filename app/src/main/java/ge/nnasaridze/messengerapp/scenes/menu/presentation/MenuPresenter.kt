package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.net.Uri
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.*
import ge.nnasaridze.messengerapp.shared.entities.ChatEntity

class MenuPresenterImpl(
    private val view: MenuView,
    private val getChatsUsecase: GetChatsUsecase = DefaultGetChatsUsecase(),
    private val signoutUsecase: SignoutUsecase = DefaultSignoutUsecase(),
    private val getUserUsecase: GetUserUsecase = DefaultGetUserUsecase(),
    private val getLastMessageUsecase: GetLastMessageUsecase = DefaultGetLastMessageUsecase(),
) : MenuPresenter {


    private var query = ""
    private var filteredData = mutableListOf<ChatEntity>()
    private val data = mutableMapOf<String, ChatEntity>()

    override fun viewInitialized() {
        view.showLoading()
        getChatsUsecase.execute(::newChatHandler)
        getUserUsecase
        view.hideLoading()
    }

    override fun fabPressed() {
        view.gotoSearch()
    }

    override fun homePressed() {
        view.setConversationsFragment()
    }

    override fun chatPressed(position: Int) {
        val chatID = filteredData[position].chatID
        view.gotoChat(chatID)
    }

    override fun searchEdited(text: String) {
        query = text
        refreshViewData()
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

    override fun imagePicked(uri: Uri?) {
        if (uri != null)
            view.setImage(uri)
    }

    private fun newChatHandler(chatID: String) {
        data[chatID] = ChatEntity(chatID)
        getUserUsecase.execute(chatID) { userEntity ->
            data[chatID]?.user = userEntity
            refreshViewData()
        }
        getLastMessageUsecase.execute(chatID) { messageEntity ->
            data[chatID]?.lastMessage = messageEntity
            refreshViewData()
        }
    }

    private fun refreshViewData() {
        filterData()
        view.updateConversations(filteredData)
    }

    private fun filterData() {
        val newFilteredData = mutableListOf<ChatEntity>()
        for (chat in data.values) {
            if (!chat.messageIsInitialized() || !chat.userIsInitialized())
                continue
            if (chat.user.nickname.contains(query))
                newFilteredData.add(chat)
        }
        newFilteredData.sortByDescending { it.lastMessage.timestamp }
        filteredData = newFilteredData
        refreshViewData()
    }
}