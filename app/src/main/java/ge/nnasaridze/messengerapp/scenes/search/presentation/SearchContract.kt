package ge.nnasaridze.messengerapp.scenes.search.presentation

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

interface SearchView {
    fun updateSearch(data: List<UserEntity>)

    fun showLoading()
    fun hideLoading()
}

interface SearchPresenter {
    fun userClicked(position: Int)
    fun searchEdited(text: String)
    fun backPressed()
    fun viewInitialized()
}