package ge.nnasaridze.messengerapp.scenes.chat.presentation

import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.RecyclerMessageEntity

interface ChatView{
    fun updateChat(data: List<RecyclerMessageEntity>)
    fun setTitle(text: String)
    fun emptyText()

    fun gotoMenu()

    fun showLoading()
    fun hideLoading()
}

interface ChatPresenter{
    fun sendPressed(text: String)
    fun backPressed()
    fun viewInitialized()
}