package ge.nnasaridze.messengerapp.scenes.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ge.nnasaridze.messengerapp.R
import ge.nnasaridze.messengerapp.databinding.FragmentConversationsBinding

class ConversationsFragment : Fragment() {
    private lateinit var binding: FragmentConversationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConversationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun updateConversations() {
        TODO("Not yet implemented")
    }
}