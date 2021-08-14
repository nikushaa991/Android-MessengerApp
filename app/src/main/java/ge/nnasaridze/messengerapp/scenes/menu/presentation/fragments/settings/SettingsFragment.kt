package ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.settings

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ge.nnasaridze.messengerapp.databinding.FragmentSettingsBinding
import ge.nnasaridze.messengerapp.shared.utils.DefaultImageLoader
import ge.nnasaridze.messengerapp.shared.utils.ImageLoader


class SettingsFragment(
    private val updateHandler: () -> Unit,
    private val signoutHandler: () -> Unit,
    private val imageHandler: () -> Unit,
    private val imageLoader: ImageLoader = DefaultImageLoader(),
) : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.settingsUpdateButton.setOnClickListener { updateHandler() }
        binding.settingsSignoutButton.setOnClickListener { signoutHandler() }
        binding.settingsIcon.setOnClickListener { imageHandler() }

        return binding.root
    }

    fun setName(name: String) {
        binding.settingsName.setText(name)
    }

    fun setProfession(profession: String) {
        binding.settingsProfession.setText(profession)
    }

    fun setImage(image: Uri) {
        imageLoader.loadImage(this, image, binding.settingsIcon)
    }

    fun getName(): String {
        return binding.settingsName.text.toString()
    }

    fun getProfession(): String {
        return binding.settingsProfession.text.toString()
    }
}