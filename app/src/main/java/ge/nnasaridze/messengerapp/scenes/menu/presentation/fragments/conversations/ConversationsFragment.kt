package ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nnasaridze.messengerapp.databinding.FragmentConversationsBinding
import ge.nnasaridze.messengerapp.shared.entities.ChatEntity
import androidx.recyclerview.widget.RecyclerView



class ConversationsFragment(
    private val onItemClickHandler: (position: Int) -> Unit,
    private val scrolledToBottomHandler: () -> Unit,
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
        adapter = ConversationsRecyclerAdapter (onItemClickHandler)
        binding.conversationsRecycler.adapter = adapter
        binding.conversationsRecycler.layoutManager = LinearLayoutManager(activity)
        binding.conversationsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && dy > 0)
                    scrolledToBottomHandler()
            }
        })
    }

    fun updateConversations(data: List<ChatEntity>) {
        adapter.setData(data)
    }
}