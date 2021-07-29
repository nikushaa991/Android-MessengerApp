package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.databinding.ActivityMenuBinding
import ge.nnasaridze.messengerapp.scenes.chat.ChatActivity
import ge.nnasaridze.messengerapp.scenes.login.MainActivity
import ge.nnasaridze.messengerapp.scenes.search.SearchActivity

class MenuActivity : MenuView, AppCompatActivity() {


    private lateinit var binding: ActivityMenuBinding
    private lateinit var presenter: MenuPresenter
    private lateinit var pager: ViewPager2
    private lateinit var conversations: ConversationsFragment
    private lateinit var settings: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth
        binding = ActivityMenuBinding.inflate(layoutInflater)
        presenter = MenuPresenterImpl(this)

        with(binding) {
            menuFab.setOnClickListener { auth.signOut() }//TODO

            conversations = ConversationsFragment(::chatPressed)
            settings = SettingsFragment(::updatePressed, ::signoutPressed, ::imagePressed)

            pager = menuViewpager
            pager.adapter = MenuFragmentsPagerAdapter(
                this@MenuActivity,
                arrayListOf(conversations, settings)
            )


        }

        supportActionBar?.hide()
        setContentView(binding.root)
    }

    override fun gotoSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    override fun setConversationsFragment() {
        pager.currentItem = FRAGMENT_CONVERSATIONS
    }

    override fun updateConversations(data: ArrayList<Int>) {
        conversations.updateConversations(data)
    }

    override fun gotoChat() {
        startActivity(Intent(this, ChatActivity::class.java))//TODO which chat?
    }

    override fun setSettingsFragment() {
        pager.currentItem = FRAGMENT_SETTINGS
    }

    override fun setName(name: String) {
        settings.setName(name)
    }

    override fun setProfession(profession: String) {
        settings.setProfession(profession)
    }

    override fun setImage(image: Int) {
        settings.setImage(image)
    }

    override fun pickImage(): Int {
        TODO("Not yet implemented")
    }

    override fun gotoLogin() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun showLoading() {
        binding.menuPb.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.menuPb.visibility = View.GONE
    }

    private fun chatPressed(position: Int) {
        presenter.chatPressed(position)
    }

    private fun updatePressed() {
        presenter.updatePressed()
    }

    private fun signoutPressed() {
        presenter.signoutPressed()
    }

    private fun imagePressed(){
        presenter.imagePressed()
    }


    class MenuFragmentsPagerAdapter(
        activity: FragmentActivity,
        private val fragments: ArrayList<Fragment>
    ) : FragmentStateAdapter(activity) {

        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int) = fragments[position]
    }

    companion object {
        const val FRAGMENT_CONVERSATIONS = 0
        const val FRAGMENT_SETTINGS = 1
    }
}