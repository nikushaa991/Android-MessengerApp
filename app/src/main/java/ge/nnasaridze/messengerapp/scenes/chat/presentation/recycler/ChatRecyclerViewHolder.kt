package ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ge.nnasaridze.messengerapp.databinding.ChatRecyclerReceivedItemBinding
import ge.nnasaridze.messengerapp.databinding.ChatRecyclerSentItemBinding
import ge.nnasaridze.messengerapp.shared.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.utils.formatChatTime

sealed class ChatRecyclerViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {


    companion object {
        const val VIEWTYPE_SENT = 0
        const val VIEWTYPE_RECEIVED = 1
    }


    class ChatRecyclerReceivedViewHolder(
        private val binding: ChatRecyclerReceivedItemBinding,
    ) : ChatRecyclerViewHolder(binding) {

        fun bind(data: RecyclerMessageEntity) {
            with(binding) {
                receivedChatText.text = data.text
                receivedChatTime.text = formatChatTime(data.timestamp)
            }
        }
    }

    class ChatRecyclerSentViewHolder(
        private val binding: ChatRecyclerSentItemBinding,
    ) : ChatRecyclerViewHolder(binding) {

        fun bind(data: RecyclerMessageEntity) {
            with(binding) {
                sentChatText.text = data.text
                sentChatTime.text = formatChatTime(data.timestamp)
            }
        }
    }
}