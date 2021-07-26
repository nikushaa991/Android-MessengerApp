package ge.nnasaridze.messengerapp.scenes.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import ge.nnasaridze.messengerapp.databinding.FragmentSettingsBinding


class SettingsFragment(
    private val updateHandler: () -> Unit,
    private val signoutHandler: () -> Unit,
    private val imageHandler: () -> Unit
) : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.settingsUpdateButton.setOnClickListener { updateHandler }
        binding.settingsSignoutButton.setOnClickListener { signoutHandler }
        binding.settingsIcon.setOnClickListener { imageHandler }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun setName(name: String) {
        binding.settingsName.setText(name)
    }

    fun setProfession(profession: String) {
        binding.settingsProfession.setText(profession)
    }

    fun setImage(image: Int) {
        binding.settingsIcon.setImageResource(image)
    }
}