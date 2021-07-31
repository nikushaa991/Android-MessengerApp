package ge.nnasaridze.messengerapp.scenes.menu.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nnasaridze.messengerapp.R
import ge.nnasaridze.messengerapp.databinding.ConversationsRecyclerItemBinding
import ge.nnasaridze.messengerapp.scenes.menu.presentation.ConversationsRecyclerAdapter.ConversationsRecyclerViewHolder
import ge.nnasaridze.messengerapp.shared.utils.formatTime
import ge.nnasaridze.messengerapp.shared.repositories.chats.ChatDTO


class ConversationsRecyclerAdapter(private val handler: (position: Int) -> Unit) :
    RecyclerView.Adapter<ConversationsRecyclerViewHolder>() {


    private var data: List<ChatDTO> = arrayListOf() //TODO

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConversationsRecyclerViewHolder {
        val vh = ConversationsRecyclerViewHolder(
            ConversationsRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        vh.itemView.setOnClickListener {    //TODO set on click
            handler(vh.adapterPosition)
        }
        return vh
    }

    override fun onBindViewHolder(
        holder: ConversationsRecyclerViewHolder,
        position: Int
    ) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<ChatDTO>) {
        data = newData
    }

    class ConversationsRecyclerViewHolder(
        private val binding: ConversationsRecyclerItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: ChatDTO) {
            with(binding) {
                conversationsName.text = data.lastMessage?.message
                conversationsProf.text = data.user?.profession
                conversationsTime.text = formatTime(data.lastMessage?.timestamp ?: 0)
                conversationsIcon.setImageResource(R.drawable.avatar_image_placeholder)
            }
        }
    }
}