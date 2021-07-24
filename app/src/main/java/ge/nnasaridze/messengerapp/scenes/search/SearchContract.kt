package ge.nnasaridze.messengerapp.scenes.search

interface SearchView {
    fun updateSearch()//TODO pass data

    fun showLoading()
    fun hideLoading()
}

interface SearchPresenter{
    fun searchEdited(text: String)
    fun backPressed()
}