package ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nnasaridze.messengerapp.databinding.FragmentConversationsBinding
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.recycler.ConversationsRecyclerAdapter
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.recycler.RecyclerChatEntity
import ge.nnasaridze.messengerapp.shared.data.entities.ChatEntity


class ConversationsFragment(
    private val onItemClickHandler: (position: Int) -> Unit,
    private val onTextChangedHandler: (text: String) -> Unit,
) : Fragment() {


    private lateinit var binding: FragmentConversationsBinding
    private lateinit var adapter: ConversationsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentConversationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ConversationsRecyclerAdapter(onItemClickHandler)
        binding.conversationsRecycler.adapter = adapter
        binding.conversationsRecycler.layoutManager = LinearLayoutManager(activity)

        binding.conversationsText.doAfterTextChanged { onTextChangedHandler(it.toString()) }
    }

    fun updateConversations(data: List<RecyclerChatEntity>) {
        adapter.setData(data)
    }
}