package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
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
    private lateinit var imagePicker: ActivityResultLauncher<String>

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
                    R.id.action_home -> presenter.homePressed()
                    else -> presenter.settingsPressed()
                }
                true
            }
            pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        FRAGMENT_CONVERSATIONS -> {
                            presenter.homePressed()
                            menuNav.selectedItemId = R.id.action_home
                        }
                        else -> {
                            presenter.settingsPressed()
                            menuNav.selectedItemId = R.id.action_settings
                        }
                    }
                }
            })
        }
        imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            presenter.imagePicked(uri)
        }
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

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
        pager.setCurrentItem(FRAGMENT_CONVERSATIONS, true)
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
        pager.setCurrentItem(FRAGMENT_SETTINGS, true)
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
        imagePicker.launch("image/*")
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