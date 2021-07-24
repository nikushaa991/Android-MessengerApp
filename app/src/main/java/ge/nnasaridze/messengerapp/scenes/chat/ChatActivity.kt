package ge.nnasaridze.messengerapp.scenes.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ge.nnasaridze.messengerapp.R

class ChatActivity : ChatView, AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }

    override fun updateChat() {
        TODO("Not yet implemented")
    }

    override fun setTitle(text: String) {
        TODO("Not yet implemented")
    }

    override fun gotoMenu() {
        TODO("Not yet implemented")
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }
}