package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.net.Uri
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.*
import ge.nnasaridze.messengerapp.shared.data.entities.ChatEntity
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.AuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.usecases.DefaultGetImageUsecase
import ge.nnasaridze.messengerapp.shared.data.usecases.DefaultGetUserUsecase
import ge.nnasaridze.messengerapp.shared.data.usecases.GetImageUsecase
import ge.nnasaridze.messengerapp.shared.data.usecases.GetUserUsecase

class MenuPresenterImpl(
    private val view: MenuView,
    private val getUserUsecase: GetUserUsecase = DefaultGetUserUsecase(),
    private val getChatIDsUsecase: GetChatIDsUsecase = DefaultGetChatIDsUsecase(),
    private val getLastMessageUsecase: GetLastMessageUsecase = DefaultGetLastMessageUsecase(),
    private val uploadImageUsecase: UploadImageUsecase = DefaultUploadImageUsecase(),
    private val getImageUsecase: GetImageUsecase = DefaultGetImageUsecase(),
    private val signoutUsecase: SignoutUsecase = DefaultSignoutUsecase(),
    private val updateUserDataUsecase: UpdateUserDataUsecase = DefaultUpdateUserDataUsecase(),
    private val authRepo: AuthenticationRepository = DefaultAuthenticationRepository(),
) : MenuPresenter {

    private val data = mutableMapOf<String, ChatEntity>()
    private var searchQuery = ""
    private var filteredData = mutableListOf<ChatEntity>()

    private lateinit var user: UserEntity
    private lateinit var newImage: Uri

    override fun viewInitialized() {
        loadChats()
    }

    override fun fabPressed() {
        view.gotoSearch()
    }

    override fun homePressed() {
        view.setConversationsFragment()
    }

    override fun chatPressed(position: Int) {
        val chatID = filteredData[position].chatID
        val recipientID = filteredData[position].user.userID
        view.gotoChat(chatID, recipientID)
    }

    override fun searchEdited(text: String) {
        searchQuery = text
        refreshViewData()
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
                updateUserDataUsecase.execute(newData) { isSuccessful ->
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

    private fun loadChats() {
        getChatIDsUsecase.execute(::newChatHandler)
    }
}