package ge.nnasaridze.messengerapp.scenes.search.presentation

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

interface SearchView {
    fun updateSearch(data: List<UserEntity>)

    fun gotoChat(chatID: String, recipientID: String)

    fun displayError(text: String)
    fun showLoading()
    fun hideLoading()

    fun goBack()
}

interface SearchPresenter {
    fun userClicked(position: Int)
    fun searchEdited(text: String)
    fun backPressed()
    fun viewInitialized()
}