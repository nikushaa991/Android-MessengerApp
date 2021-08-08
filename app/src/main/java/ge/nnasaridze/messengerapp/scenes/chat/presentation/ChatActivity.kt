package ge.nnasaridze.messengerapp.scenes.chat.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nnasaridze.messengerapp.databinding.ActivityChatBinding
import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.ChatRecyclerAdapter
import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.RecyclerMessageEntity

class ChatActivity : ChatView, AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var presenter: ChatPresenter
    private lateinit var adapter: ChatRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        presenter = ChatPresenterImpl(this,
            intent.getStringExtra("chatID")!!,
            intent.getStringExtra("recipientID")!!)

        with(binding) {
            adapter = ChatRecyclerAdapter()
            chatRecycler.adapter = adapter
            chatRecycler.adapter = ChatRecyclerAdapter()
            chatRecycler.layoutManager =
                LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, true)

            chatSend.setOnClickListener { sendPressed() }
            chatToolbar.setNavigationOnClickListener { presenter.backPressed() }
        }

        presenter.viewInitialized()

        supportActionBar?.hide()
        setContentView(binding.root)
    }


    override fun updateChat(data: List<RecyclerMessageEntity>) {
        adapter.setData(data)
    }

    override fun setTitle(text: String) {
        binding.chatToolbar.title = text
    }

    override fun emptyText() {
        binding.chatText.setText("")
    }

    override fun getText(): String {
        return binding.chatText.text.toString()
    }

    override fun closeChat() {
        finish()
    }

    override fun showLoading() {
        binding.chatPb.visibility = View.VISIBLE
    }

    override fun displayError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun hideLoading() {
        binding.chatPb.visibility = View.GONE
    }

    private fun sendPressed() {
        presenter.sendPressed()
    }
}