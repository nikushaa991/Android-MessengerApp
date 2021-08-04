package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.net.Uri
import ge.nnasaridze.messengerapp.shared.entities.ChatEntity

interface MenuView {
    fun displayError(text: String)
    fun gotoSearch()
    fun setConversationsFragment()
    fun updateConversations(data: MutableList<ChatEntity>)
    fun gotoChat(chatID: String)

    fun setSettingsFragment()
    fun setName(name: String)
    fun setProfession(profession: String)
    fun setImage(image: Uri)
    fun pickImage()
    fun gotoLogin()

    fun showLoading()
    fun hideLoading()
}

interface MenuPresenter {
    fun viewInitialized()
    fun fabPressed()

    fun homePressed()
    fun chatPressed(position: Int)
    fun searchEdited(text: String)

    fun settingsPressed()
    fun updatePressed()
    fun signoutPressed()
    fun imagePressed()
    fun imagePicked(uri: Uri?)


}