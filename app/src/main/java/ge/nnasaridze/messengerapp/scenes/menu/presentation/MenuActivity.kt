package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import ge.nnasaridze.messengerapp.databinding.ActivityMenuBinding
import ge.nnasaridze.messengerapp.scenes.chat.ChatActivity
import ge.nnasaridze.messengerapp.scenes.login.presentation.MainActivity
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.ConversationsFragment
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.settings.SettingsFragment
import ge.nnasaridze.messengerapp.scenes.search.SearchActivity
import ge.nnasaridze.messengerapp.shared.entities.ChatEntity

class MenuActivity : MenuView, AppCompatActivity() {

    class MenuFragmentsPagerAdapter(
        activity: FragmentActivity,
        private val fragments: ArrayList<Fragment>,
    ) : FragmentStateAdapter(activity) {

        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int) = fragments[position]
    }

    companion object {
        const val FRAGMENT_CONVERSATIONS = 0
        const val FRAGMENT_SETTINGS = 1
    }

    private lateinit var binding: ActivityMenuBinding
    private lateinit var presenter: MenuPresenter
    private lateinit var pager: ViewPager2
    private lateinit var conversations: ConversationsFragment
    private lateinit var settings: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        presenter = MenuPresenterImpl(this)

        with(binding) {
            menuFab.setOnClickListener { fabPressed() }

            conversations = ConversationsFragment(::chatPressed, ::scrolledToBottom)
            settings = SettingsFragment(::updatePressed, ::signoutPressed, ::imagePressed)

            pager = menuViewpager
            pager.adapter = MenuFragmentsPagerAdapter(
                this@MenuActivity,
                arrayListOf(conversations, settings)
            )
        }

        presenter.viewInitialized()

        supportActionBar?.hide()
        setContentView(binding.root)
    }

    override fun displayError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun gotoSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    override fun setConversationsFragment() {
        pager.currentItem = FRAGMENT_CONVERSATIONS
    }

    override fun updateConversations(data: MutableList<ChatEntity>) {
        conversations.updateConversations(data)

    }

    override fun gotoChat(chatID: String) {
        startActivity(
            Intent(this, ChatActivity::class.java)
                .putExtra("chatID", chatID)
        )
    }

    override fun setSettingsFragment() {
        pager.currentItem = FRAGMENT_SETTINGS
    }

    override fun setName(name: String) {
        settings.setName(name)
    }

    override fun getName(): String {
        return settings.getName()
    }

    override fun setProfession(profession: String) {
        settings.setProfession(profession)
    }

    override fun getProfession(): String {
        return settings.getProfession()
    }

    override fun setImage(image: Uri) {
        settings.setImage(image)
    }

    override fun pickImage() {
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            presenter.imagePicked(uri)
        }.launch("image/*")
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


    private fun fabPressed() {
        presenter.fabPressed()
    }

    private fun chatPressed(position: Int) {
        presenter.chatPressed(position)
    }

    private fun scrolledToBottom() {
        presenter.scrolledToBottom()
    }

    private fun updatePressed() {
        presenter.updatePressed()
    }

    private fun signoutPressed() {
        presenter.signoutPressed()
    }

    private fun imagePressed() {
        presenter.imagePressed()
    }
}