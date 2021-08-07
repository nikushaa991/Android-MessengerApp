package ge.nnasaridze.messengerapp.scenes.search.presentation

import ge.nnasaridze.messengerapp.scenes.search.data.usecases.DefaultGetUsersUsecase
import ge.nnasaridze.messengerapp.scenes.search.data.usecases.GetUsersUsecase
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.utils.LAZY_LOADING_AMOUNT

class SearchPresenterImpl(
    private val view: SearchView,
    private val getUsersUsecase: GetUsersUsecase = DefaultGetUsersUsecase(),
) : SearchPresenter {


    private val data = mutableListOf<UserEntity>()
    private var filteredData = mutableListOf<UserEntity>()
    private var usersToLoad = 0

    override fun viewInitialized() {
        view.showLoading()
        loadUsers()
    }

    override fun userClicked(position: Int) {
        val userID = filteredData[position].userID
        //TODO create new chat and get id
        //view.gotoChat(chatID, recipientID)
    }

    override fun searchEdited(text: String) {
        //TODO debounce maybe?
    }

    override fun backPressed() {
        TODO("Not yet implemented")
    }

    private fun loadUsers() {
        if (data.size < usersToLoad)
            return
        usersToLoad += LAZY_LOADING_AMOUNT
        getUsersUsecase.execute(
            amount = usersToLoad, newUserHandler = ::newUserHandler,
            errorHandler = ::errorHandler)
    }

    private fun newUserHandler(user: UserEntity) {

    }

    private fun onScrolledToBottom() {

    }

    private fun errorHandler(text: String) {
        view.displayError(text)
    }
}