package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.net.Uri
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.*
import ge.nnasaridze.messengerapp.shared.entities.ChatEntity
import ge.nnasaridze.messengerapp.shared.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.repositories.authentication.AuthenticationRepository
import ge.nnasaridze.messengerapp.shared.repositories.authentication.DefaultAuthenticationRepository

class MenuPresenterImpl(
    private val view: MenuView,
    private val getUserUsecase: GetUserUsecase = DefaultGetUserUsecase(),
    private val getChatsUsecase: GetChatsUsecase = DefaultGetChatsUsecase(),
    private val getLastMessageUsecase: GetLastMessageUsecase = DefaultGetLastMessageUsecase(),
    private val uploadImageUsecase: UploadImageUsecase = DefaultUploadImageUsecase(),
    private val getImageUsecase: GetImageUsecase = DefaultGetImageUsecase(),
    private val signoutUsecase: SignoutUsecase = DefaultSignoutUsecase(),
    private val authRepo: AuthenticationRepository = DefaultAuthenticationRepository(),
) : MenuPresenter {


    private var searchQuery = ""
    private var filteredData = mutableListOf<ChatEntity>()
    private val data = mutableMapOf<String, ChatEntity>()
    private lateinit var user: UserEntity
    private lateinit var newImage: Uri

    override fun viewInitialized() {
        view.showLoading()
        getChatsUsecase.execute(::newChatHandler)
        getUserUsecase.execute(authRepo.getID(), ::setupUserData)
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
        searchQuery = text
        refreshViewData()
    }

    override fun settingsPressed() {
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
        if (!::user.isInitialized)
            getImageUsecase.execute(userEntity.userID) { uri ->
                view.setImage(uri)
            }

        user = userEntity
        view.setName(user.nickname)
        view.setProfession(user.profession)
    }
}