package ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nnasaridze.messengerapp.databinding.FragmentConversationsBinding

import ge.nnasaridze.messengerapp.shared.entities.ChatEntity


class ConversationsFragment(
    private val onItemClickHandler: (position: Int) -> Unit,
) : Fragment() {


    private lateinit var binding: FragmentConversationsBinding
    private lateinit var adapter: ConversationsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConversationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.conversationsRecycler.adapter = ConversationsRecyclerAdapter(onItemClickHandler)
        binding.conversationsRecycler.layoutManager = LinearLayoutManager(activity)
    }

    fun updateConversations(data: List<ChatEntity>) {
        adapter.setData(data)
    }
}