package ge.nnasaridze.messengerapp.scenes.menu

interface MenuView{
    fun gotoSearch()

    fun setConversationsFragment()
    fun updateConversations()//TODO pass data
    fun gotoChat()

    fun setSettingsFragment()
    fun setName(name: String)
    fun setProfession(profession: String)
    fun setImage(image: Int)//TODO drawable?
    fun pickImage() : Int//TODO idk how to yet
    fun gotoLogin()//TODO signout?

    fun showLoading()
    fun hideLoading()
}

interface MenuPresenter{
    fun fabPressed()

    fun homePressed()
    fun chatPressed(position: Int)
    fun searchEdited(text: String)

    fun settingsPressed()
    fun updatePressed()
    fun signoutPressed()
    fun imagePressed()
}