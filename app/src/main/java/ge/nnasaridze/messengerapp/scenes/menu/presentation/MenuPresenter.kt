package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.net.Uri
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.*
import ge.nnasaridze.messengerapp.shared.entities.ChatEntity
import ge.nnasaridze.messengerapp.shared.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.repositories.authentication.AuthenticationRepository
import ge.nnasaridze.messengerapp.shared.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.utils.LAZY_LOADING_AMOUNT

class MenuPresenterImpl(
    private val view: MenuView,
    private val getUserUsecase: GetUserUsecase = DefaultGetUserUsecase(),
    private val getChatsUsecase: GetChatsUsecase = DefaultGetChatsUsecase(),
    private val getLastMessageUsecase: GetLastMessageUsecase = DefaultGetLastMessageUsecase(),
    private val uploadImageUsecase: UploadImageUsecase = DefaultUploadImageUsecase(),
    private val getImageUsecase: GetImageUsecase = DefaultGetImageUsecase(),
    private val signoutUsecase: SignoutUsecase = DefaultSignoutUsecase(),
    private val updateDataUsecase: UpdateDataUsecase = DefaultUpdateDataUsecase(),
    private val authRepo: AuthenticationRepository = DefaultAuthenticationRepository(),
) : MenuPresenter {

    private val data = mutableMapOf<String, ChatEntity>()
    private var searchQuery = ""
    private var filteredData = mutableListOf<ChatEntity>()
    private var chatAmount = 0

    private lateinit var user: UserEntity
    private lateinit var newImage: Uri

    override fun viewInitialized() {
        loadNewChats()
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
        searchQuery = text
        refreshViewData()
    }

    override fun scrolledToBottom() {
        loadNewChats()
    }

    override fun settingsPressed() {
        if (!::user.isInitialized) {
            view.showLoading()
            getUserUsecase.execute(authRepo.getID(), ::setupUserData)
        }
        view.setSettingsFragment()
    }

    override fun updatePressed() {
        if (::newImage.isInitialized) {
            view.showLoading()
            uploadImageUsecase.execute(newImage) { isSuccessful ->
                if (!isSuccessful)
                    view.displayError("Image upload failed")
                view.hideLoading()
            }
        }
        if (::user.isInitialized) {
            val newData = UserEntity(user.userID, view.getName(), view.getProfession())
            if (user.nickname != newData.nickname || user.profession != newData.profession) {
                view.showLoading()
                updateDataUsecase.execute(newData) { isSuccessful ->
                    if (!isSuccessful)
                        view.displayError("Update Failed")
                    view.hideLoading()
                }
            }
        }
    }

    override fun signoutPressed() {
        signoutUsecase.execute()
        view.gotoLogin()
    }

    override fun imagePressed() {
        view.pickImage()
    }

    override fun imagePicked(uri: Uri?) {
        if (uri == null)
            return
        newImage = uri
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

        if (data.size == chatAmount)
            view.hideLoading()
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
            if (chat.user.nickname.contains(searchQuery))
                newFilteredData.add(chat)
        }
        newFilteredData.sortByDescending { it.lastMessage.timestamp }
        filteredData = newFilteredData
        refreshViewData()
    }

    private fun setupUserData(userEntity: UserEntity) {
        view.setName(user.nickname)
        view.setProfession(user.profession)

        if (!::user.isInitialized) {
            view.hideLoading()
            getImageUsecase.execute(userEntity.userID) { uri ->
                view.setImage(uri)
            }
        }
        user = userEntity
    }

    private fun loadNewChats() {
        if (data.size < chatAmount)
            return
        view.showLoading()
        chatAmount += LAZY_LOADING_AMOUNT
        getChatsUsecase.execute(chatAmount, ::newChatHandler)
    }
}