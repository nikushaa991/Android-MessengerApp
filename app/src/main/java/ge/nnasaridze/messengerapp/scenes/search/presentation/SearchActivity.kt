package ge.nnasaridze.messengerapp.scenes.search.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.nnasaridze.messengerapp.databinding.ActivitySearchBinding
import ge.nnasaridze.messengerapp.scenes.chat.presentation.ChatActivity
import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.ChatRecyclerAdapter
import ge.nnasaridze.messengerapp.scenes.search.presentation.recycler.RecyclerUserEntity
import ge.nnasaridze.messengerapp.scenes.search.presentation.recycler.SearchRecyclerAdapter


class SearchActivity : SearchView, AppCompatActivity() {


    private lateinit var binding: ActivitySearchBinding
    private lateinit var presenter: SearchPresenter
    private lateinit var adapter: SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        presenter = SearchPresenterImpl(this)

        with(binding) {
            adapter = SearchRecyclerAdapter(::onUserClicked)
            searchRecycler.adapter = adapter
            searchRecycler.adapter = ChatRecyclerAdapter()
            searchRecycler.layoutManager = LinearLayoutManager(this@SearchActivity)

            searchRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(1) && dy > 0)
                        presenter.onScrolledToBottom()
                }
            })

            searchBackButton.setOnClickListener { backPressed() }

            searchText.doAfterTextChanged { onTextChanged(it.toString()) }
        }

        presenter.viewInitialized()

        supportActionBar?.hide()
        setContentView(binding.root)
    }

    override fun updateSearch(data: List<RecyclerUserEntity>) {
        adapter.setData(data)
    }

    override fun gotoChat(chatID: String, recipientID: String) {
        startActivity(
            Intent(this, ChatActivity::class.java)
                .putExtra("chatID", chatID)
                .putExtra("recipientID", recipientID)
        )
    }

    override fun displayError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        binding.searchPb.visibility = VISIBLE
    }

    override fun hideLoading() {
        binding.searchPb.visibility = GONE
    }

    override fun goBack() {
        finish()
    }

    private fun backPressed() {
        presenter.backPressed()
    }

    private fun onUserClicked(position: Int) {
        presenter.userClicked(position)
    }

    private fun onTextChanged(text: String) {
        presenter.searchEdited(text)
    }


}