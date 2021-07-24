package ge.nnasaridze.messengerapp.scenes.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ge.nnasaridze.messengerapp.R

class SearchActivity : SearchView, AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    override fun updateSearch() {
        TODO("Not yet implemented")
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }
}