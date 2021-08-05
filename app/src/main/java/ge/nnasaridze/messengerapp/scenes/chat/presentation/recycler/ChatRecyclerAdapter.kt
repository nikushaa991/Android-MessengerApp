package ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nnasaridze.messengerapp.databinding.ChatRecyclerReceivedItemBinding
import ge.nnasaridze.messengerapp.databinding.ChatRecyclerSentItemBinding
import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.ChatRecyclerViewHolder.Companion.VIEWTYPE_SENT
import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.ChatRecyclerViewHolder.Companion.VIEWTYPE_RECEIVED


class ChatRecyclerAdapter : RecyclerView.Adapter<ChatRecyclerViewHolder>() {
    private var data: List<RecyclerMessageEntity> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ChatRecyclerViewHolder {
        return if (viewType == VIEWTYPE_SENT) {
            ChatRecyclerViewHolder.ChatRecyclerSentViewHolder(
                ChatRecyclerSentItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false))
        } else {
            ChatRecyclerViewHolder.ChatRecyclerReceivedViewHolder(
                ChatRecyclerReceivedItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(
        holder: ChatRecyclerViewHolder,
        position: Int,
    ) {
        when (holder) {
            is ChatRecyclerViewHolder.ChatRecyclerSentViewHolder -> holder.bind(data[position])
            is ChatRecyclerViewHolder.ChatRecyclerReceivedViewHolder -> holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].sent) {
            true -> VIEWTYPE_SENT
            false -> VIEWTYPE_RECEIVED
        }
    }

    fun setData(newData: List<RecyclerMessageEntity>) {
        data = newData
    }
}