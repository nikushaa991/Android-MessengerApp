package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import ge.nnasaridze.messengerapp.R
import ge.nnasaridze.messengerapp.databinding.ActivityMenuBinding
import ge.nnasaridze.messengerapp.scenes.chat.presentation.ChatActivity
import ge.nnasaridze.messengerapp.scenes.login.presentation.MainActivity
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.ConversationsFragment
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.recycler.RecyclerChatEntity
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.settings.SettingsFragment
import ge.nnasaridze.messengerapp.scenes.search.presentation.SearchActivity

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

            conversations = ConversationsFragment(::chatPressed, ::onTextChanged)
            settings = SettingsFragment(::updatePressed, ::signoutPressed, ::imagePressed)

            pager = menuViewpager
            pager.adapter = MenuFragmentsPagerAdapter(
                this@MenuActivity,
                arrayListOf(conversations, settings)
            )

            menuNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_home -> pager.setCurrentItem(FRAGMENT_CONVERSATIONS, true)
                    else -> pager.setCurrentItem(FRAGMENT_SETTINGS, true)
                }
                true
            }
            pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    menuNav.selectedItemId = when (position) {
                        FRAGMENT_CONVERSATIONS -> R.id.action_home
                        else -> R.id.action_settings
                    }
                }
            })
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

    override fun updateConversations(data: List<RecyclerChatEntity>) {
        conversations.updateConversations(data)

    }

    override fun gotoChat(chatID: String, recipientID: String) {
        startActivity(
            Intent(this, ChatActivity::class.java)
                .putExtra("chatID", chatID)
                .putExtra("recipientID", recipientID)
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
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        })
        this.finish()
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

    private fun onTextChanged(text: String) {
        presenter.searchEdited(text)
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