package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nnasaridze.messengerapp.databinding.FragmentConversationsBinding

class ConversationsFragment(private val handler: (position: Int) -> Unit) : Fragment() {


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

        binding.conversationsRecycler.adapter = ConversationsRecyclerAdapter(handler)
        binding.conversationsRecycler.layoutManager = LinearLayoutManager(activity)

    }

    fun updateConversations(data: ArrayList<Int>) {
        adapter.setData(data)
    }
}