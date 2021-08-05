package ge.nnasaridze.messengerapp.scenes.chat.presentation

import android.os.Bundle
import android.view.View
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
        presenter = ChatPresenterImpl(this, intent.getStringExtra("chatID")!!)

        with(binding) {
            adapter = ChatRecyclerAdapter()
            chatRecycler.adapter = adapter
            chatRecycler.adapter = ChatRecyclerAdapter()
            chatRecycler.layoutManager =
                LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)

            chatSend.setOnClickListener { sendPressed() }
            //todo back pressed, scroll listener + scroll to bottom?
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

    override fun gotoMenu() {
        finish()
    }

    override fun showLoading() {
        binding.chatPb.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.chatPb.visibility = View.GONE
    }

    private fun sendPressed() {
        presenter.sendPressed(binding.chatText.text.toString())
    }
}