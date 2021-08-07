package ge.nnasaridze.messengerapp.scenes.chat.presentation

import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.RecyclerMessageEntity

interface ChatView {
    fun updateChat(data: List<RecyclerMessageEntity>)
    fun setTitle(text: String)
    fun emptyText()
    fun getText(): String

    fun closeChat()

    fun displayError(text: String)
    fun showLoading()
    fun hideLoading()
}

interface ChatPresenter {
    fun sendPressed()
    fun backPressed()
    fun viewInitialized()
}