package ge.nnasaridze.messengerapp.scenes.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.R
import ge.nnasaridze.messengerapp.databinding.ActivityMenuBinding

class MenuActivity : MenuView, AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var pager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth
        binding = ActivityMenuBinding.inflate(layoutInflater)

        with(binding) {
            menuFab.setOnClickListener { auth.signOut() }//TODO
            pager = menuViewpager

            pager.adapter = MenuFragmentsPagerAdapter(
                this@MenuActivity,
                arrayListOf(ConversationsFragment(), SettingsFragment())
            )

        }

        supportActionBar?.hide()
        setContentView(binding.root)
    }

    override fun gotoSearch() {
        TODO("Not yet implemented")
    }

    override fun setConversationsFragment() {
        pager.currentItem = FRAGMENT_CONVERSATIONS
    }

    override fun updateConversations() {
        TODO("Not yet implemented")
    }

    override fun gotoChat() {
        TODO("Not yet implemented")
    }

    override fun setSettingsFragment() {
        pager.currentItem = FRAGMENT_SETTINGS
    }

    override fun setName(name: String) {
        TODO("Not yet implemented")
    }

    override fun setProfession(profession: String) {
        TODO("Not yet implemented")
    }

    override fun setImage(image: Int) {
        TODO("Not yet implemented")
    }

    override fun pickImage(): Int {
        TODO("Not yet implemented")
    }

    override fun gotoLogin() {
        TODO("Not yet implemented")
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    fun chatPressed(position: Int) {}

    fun updatePressed() {}

    fun signoutPressed() {}


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