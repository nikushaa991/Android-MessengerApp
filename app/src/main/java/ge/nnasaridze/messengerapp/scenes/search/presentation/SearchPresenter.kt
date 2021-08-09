package ge.nnasaridze.messengerapp.scenes.search.presentation

import ge.nnasaridze.messengerapp.scenes.search.data.usecases.CreateChatUsecase
import ge.nnasaridze.messengerapp.scenes.search.data.usecases.DefaultCreateChatUsecase
import ge.nnasaridze.messengerapp.scenes.search.data.usecases.DefaultGetUsersUsecase
import ge.nnasaridze.messengerapp.scenes.search.data.usecases.GetUsersUsecase
import ge.nnasaridze.messengerapp.scenes.search.presentation.recycler.RecyclerUserEntity
import ge.nnasaridze.messengerapp.shared.utils.LAZY_LOADING_AMOUNT

class SearchPresenterImpl(
    private val view: SearchView,
    private val getUsersUsecase: GetUsersUsecase = DefaultGetUsersUsecase(),
    private val createChatUsecase: CreateChatUsecase = DefaultCreateChatUsecase(),
) : SearchPresenter {


    private val data = mutableListOf<RecyclerUserEntity>()
    private var searchedData = mutableListOf<RecyclerUserEntity>()
    private var usersToLoad = 0

    private var searchQuery = ""
    private var isSearching = false

    override fun viewInitialized() {
        view.showLoading()
        loadUsers()
    }

    override fun userClicked(position: Int) {
        val recipientID = (if (isSearching) searchedData else data)[position].userID
        createChatUsecase.execute(
            recipientID = recipientID,
            onSuccessHandler = { chatID -> view.gotoChat(chatID, recipientID) },
            errorHandler = ::errorHandler)
    }

    override fun searchEdited(text: String) {
        isSearching = text.length >= 3
        searchQuery = text
        if (text.length < 3) {
            view.updateSearch(data)
            return
        }
        searchedData.clear()
        view.updateSearch(searchedData)
        getUsersUsecase.execute(
            nameQuery = searchQuery,
            newUserHandler = ::newSearchedUserHandler,
            errorHandler = ::errorHandler)
    }

    override fun backPressed() {
        view.goBack()
    }

    private fun loadUsers() {
        if (isSearching)
            return
        if (data.size < usersToLoad)
            return
        usersToLoad += LAZY_LOADING_AMOUNT
        getUsersUsecase.execute(
            amount = usersToLoad,
            newUserHandler = ::newUserHandler,
            errorHandler = ::errorHandler)
    }

    override fun onScrolledToBottom() {
        loadUsers()
    }

    private fun newUserHandler(user: RecyclerUserEntity) {
        view.hideLoading()
        data.add(user)

        if (!isSearching)
            view.updateSearch(data)
    }

    private fun newSearchedUserHandler(user: RecyclerUserEntity, query: String) {
        if (query != searchQuery)
            return

        searchedData.add(user)

        if (isSearching)
            view.updateSearch(searchedData)
    }

    private fun errorHandler(text: String) {
        view.hideLoading()
        view.displayError(text)
    }
}