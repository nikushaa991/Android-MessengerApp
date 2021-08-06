package ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nnasaridze.messengerapp.R
import ge.nnasaridze.messengerapp.databinding.ConversationsRecyclerItemBinding
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.ConversationsRecyclerAdapter.ConversationsRecyclerViewHolder
import ge.nnasaridze.messengerapp.shared.data.entities.ChatEntity
import ge.nnasaridze.messengerapp.shared.utils.formatTime


class ConversationsRecyclerAdapter(private val handler: (position: Int) -> Unit) :
    RecyclerView.Adapter<ConversationsRecyclerViewHolder>() {


    private var data: List<ChatEntity> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ConversationsRecyclerViewHolder {
        val vh = ConversationsRecyclerViewHolder(
            ConversationsRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        vh.itemView.setOnClickListener {
            handler(vh.adapterPosition)
        }
        return vh
    }

    override fun onBindViewHolder(
        holder: ConversationsRecyclerViewHolder,
        position: Int,
    ) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<ChatEntity>) {
        data = newData
    }

    class ConversationsRecyclerViewHolder(
        private val binding: ConversationsRecyclerItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: ChatEntity) {
            with(binding) {
                conversationsName.text = data.lastMessage.text
                conversationsProf.text = data.user.profession
                conversationsTime.text = formatTime(data.lastMessage.timestamp)
                conversationsIcon.setImageResource(R.drawable.avatar_image_placeholder)
            }
        }
    }
}