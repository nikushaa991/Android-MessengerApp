package ge.nnasaridze.messengerapp.scenes.search.presentation

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nnasaridze.messengerapp.databinding.ActivitySearchBinding
import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.ChatRecyclerAdapter
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

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

            searchBackButton.setOnClickListener { backPressed() }

            searchSearch.doAfterTextChanged { onTextChanged(it.toString()) }
            //todo search behavior
        }

        presenter.viewInitialized()

        supportActionBar?.hide()
        setContentView(binding.root)
    }

    override fun updateSearch(data: List<UserEntity>) {
        adapter.setData(data)
    }

    override fun showLoading() {
        binding.searchPb.visibility = VISIBLE
    }

    override fun hideLoading() {
        binding.searchPb.visibility = GONE
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