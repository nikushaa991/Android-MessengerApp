package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.net.Uri
import ge.nnasaridze.messengerapp.scenes.menu.data.usecases.*
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.recycler.RecyclerChatEntity
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

class MenuPresenterImpl(
    private val view: MenuView,
    private val subscribeChatsUsecase: SubscribeChatsUsecase = DefaultSubscribeChatsUsecase(),
    private val subscribeCurrentUserUsecase: SubscribeCurrentUserUsecase = DefaultSubscribeCurrentUserUsecase(),
    private val getImageUsecase: GetImageUsecase = DefaultGetImageUsecase(),
    private val uploadImageUsecase: UploadImageUsecase = DefaultUploadImageUsecase(),
    private val updateUserDataUsecase: UpdateUserDataUsecase = DefaultUpdateUserDataUsecase(),
    private val signoutUsecase: SignoutUsecase = DefaultSignoutUsecase(),
) : MenuPresenter {

    private val data = mutableMapOf<String, RecyclerChatEntity>()
    private var searchQuery = ""
    private var filteredData = mutableListOf<RecyclerChatEntity>()

    private lateinit var user: UserEntity
    private lateinit var newImage: Uri

    override fun viewInitialized() {
        subscribeChatsUsecase.execute(
            newChatHandler = ::newChatHandler,
            errorHandler = ::errorHandler,
        )
    }

    override fun fragmentInitialized() {
        refreshViewData()
    }

    override fun fabPressed() {
        view.gotoSearch()
    }

    override fun homePressed() {
        view.setConversationsFragment()
    }

    override fun chatPressed(position: Int) {
        val chat = filteredData[position]
        view.gotoChat(chat.chatID, chat.user.userID)
    }

    override fun searchEdited(text: String) {
        searchQuery = text
        refreshViewData()
    }

    override fun settingsPressed() {
        if (!::user.isInitialized) {
            view.showLoading()
            subscribeCurrentUserUsecase.execute(
                onCompleteHandler = ::setupUserData,
                errorHandler = ::errorHandler)
        }
        view.setSettingsFragment()
    }

    override fun updatePressed() {
        if (::newImage.isInitialized)
            uploadImage()

        val localName = view.getName()
        val localProf = view.getProfession()
        if (::user.isInitialized && (user.nickname != localName || user.profession != localProf))
            updateUserData(localName, localProf)
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

    private fun newChatHandler(chat: RecyclerChatEntity) {
        data[chat.chatID] = chat
        if (data.size == 1)
            view.hideLoading()
        refreshViewData()
    }

    private fun errorHandler(text: String) {
        view.hideLoading()
        view.displayError(text)
    }

    private fun refreshViewData() {
        filterData()
        view.updateConversations(filteredData)
    }

    private fun filterData() {
        val newFilteredData = mutableListOf<RecyclerChatEntity>()
        for (chat in data.values)
            if (chat.user.nickname.contains(searchQuery))
                newFilteredData.add(chat)

        newFilteredData.sortByDescending { it.lastMessage.timestamp }
        filteredData = newFilteredData
    }

    private fun setupUserData(userEntity: UserEntity) {
        view.setName(userEntity.nickname)
        view.setProfession(userEntity.profession)

        if (!::user.isInitialized) {
            view.hideLoading()
            getImageUsecase.execute(
                id = userEntity.userID,
                onSuccessHandler = { view.setImage(it) },
                errorHandler = ::errorHandler,
            )
        }
        user = userEntity
    }

    private fun uploadImage() {
        view.showLoading()
        uploadImageUsecase.execute(
            uri = newImage,
            onCompleteHandler = { view.hideLoading() },
            errorHandler = ::errorHandler)

    }

    private fun updateUserData(name: String, prof: String) {
        val newUser = UserEntity(user.userID, name, prof)
        view.showLoading()
        updateUserDataUsecase.execute(
            user = newUser,
            onCompleteHandler = { view.hideLoading() },
            errorHandler = ::errorHandler)
    }
}
