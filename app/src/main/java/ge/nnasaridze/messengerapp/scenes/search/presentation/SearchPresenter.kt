package ge.nnasaridze.messengerapp.scenes.search.presentation

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

class SearchPresenterImpl(
    private val view: SearchView,
) : SearchPresenter {

    private val data = mutableListOf<UserEntity>()
    private val filteredData = mutableListOf<UserEntity>()

    override fun viewInitialized() {
        TODO("Not yet implemented")
    }

    override fun userClicked(position: Int) {
        TODO("Not yet implemented")
    }

    override fun searchEdited(text: String) {
        TODO("Not yet implemented")
    }

    override fun backPressed() {
        TODO("Not yet implemented")
    }

}