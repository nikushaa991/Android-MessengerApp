package ge.nnasaridze.messengerapp.scenes.chat

interface ChatView{
    fun updateChat()//TODO pass data
    fun setTitle(text: String)

    fun gotoMenu()

    fun showLoading()
    fun hideLoading()
}

interface ChatPresenter{
    fun sendPressed(text: String)
    fun backPressed()
}