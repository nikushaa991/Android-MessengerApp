package ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.nnasaridze.messengerapp.R
import ge.nnasaridze.messengerapp.databinding.ConversationsRecyclerItemBinding
import ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.recycler.ConversationsRecyclerAdapter.ConversationsRecyclerViewHolder
import ge.nnasaridze.messengerapp.shared.utils.formatChatTime


class ConversationsRecyclerAdapter(private val handler: (position: Int) -> Unit) :
    RecyclerView.Adapter<ConversationsRecyclerViewHolder>() {


    private var data: List<RecyclerChatEntity> = arrayListOf()

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

    fun setData(newData: List<RecyclerChatEntity>) {
        data = newData
        notifyDataSetChanged()
    }

    class ConversationsRecyclerViewHolder(
        private val binding: ConversationsRecyclerItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: RecyclerChatEntity) {
            with(binding) {
                conversationsName.text = data.user.nickname
                conversationsProf.text = data.lastMessage.text
                conversationsTime.text = formatChatTime(data.lastMessage.timestamp)
                conversationsIcon.setImageResource(R.drawable.avatar_image_placeholder)

                Glide.with(itemView.context)
                    .load(data.image)
                    .placeholder(R.drawable.avatar_image_placeholder)
                    .error(R.drawable.avatar_image_placeholder)
                    .into(conversationsIcon)
            }
        }
    }
}