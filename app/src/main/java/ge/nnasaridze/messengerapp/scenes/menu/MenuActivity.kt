package ge.nnasaridze.messengerapp.scenes.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ge.nnasaridze.messengerapp.R

class MenuActivity : MenuView, AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    override fun gotoSearch() {
        TODO("Not yet implemented")
    }

    override fun setConversationsFragment() {
        TODO("Not yet implemented")
    }

    override fun updateConversations() {
        TODO("Not yet implemented")
    }

    override fun gotoChat() {
        TODO("Not yet implemented")
    }

    override fun setSettingsFragment() {
        TODO("Not yet implemented")
    }

    override fun setName(name: String) {
        TODO("Not yet implemented")
    }

    override fun setProfession(profession: String) {
        TODO("Not yet implemented")
    }

    override fun setImage(image: Int) {
        TODO("Not yet implemented")
    }

    override fun pickImage(): Int {
        TODO("Not yet implemented")
    }

    override fun gotoLogin() {
        TODO("Not yet implemented")
    }

    fun chatPressed(position : Int) {}

    fun updatePressed(){}

    fun signoutPressed(){}
}