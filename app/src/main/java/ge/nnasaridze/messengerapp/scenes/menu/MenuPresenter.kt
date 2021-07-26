package ge.nnasaridze.messengerapp.scenes.menu

class MenuPresenterImpl(private val view: MenuView) : MenuPresenter {
    override fun fabPressed() {
        view.gotoSearch()
    }

    override fun homePressed() {
        view.setConversationsFragment()
    }

    override fun chatPressed(position: Int) {
        view.gotoChat()
    }

    override fun searchEdited(text: String) {
        //TODO search logic
    }

    override fun settingsPressed() {
        view.setSettingsFragment()
    }

    override fun updatePressed() {
        TODO("Not yet implemented")
    }

    override fun signoutPressed() {
        TODO("Not yet implemented")
    }

    override fun imagePressed() {
        view.pickImage()
    }
}