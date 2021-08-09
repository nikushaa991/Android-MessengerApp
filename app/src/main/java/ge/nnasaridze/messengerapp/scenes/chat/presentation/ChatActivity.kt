package ge.nnasaridze.messengerapp.scenes.chat.presentation

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.nnasaridze.messengerapp.databinding.ActivityChatBinding
import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.ChatRecyclerAdapter
import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.RecyclerMessageEntity

class ChatActivity : ChatView, AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var presenter: ChatPresenter
    private lateinit var adapter: ChatRecyclerAdapter
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        presenter = ChatPresenterImpl(this,
            intent.getStringExtra("chatID")!!,
            intent.getStringExtra("recipientID")!!)

        with(binding) {
            recycler = chatRecycler
            adapter = ChatRecyclerAdapter()
            recycler.adapter = adapter
            recycler.layoutManager =
                LinearLayoutManager(this@ChatActivity).apply {
                    stackFromEnd = true
                }
            chatSend.setOnClickListener { sendPressed() }
            chatToolbar.setNavigationOnClickListener { presenter.backPressed() }
        }

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        presenter.viewInitialized()

        supportActionBar?.hide()
        setContentView(binding.root)
    }


    override fun updateChat(data: List<RecyclerMessageEntity>) {
        adapter.setData(data)
        recycler.smoothScrollToPosition(data.size)
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