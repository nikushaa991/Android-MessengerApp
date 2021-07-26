package ge.nnasaridze.messengerapp.scenes.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nnasaridze.messengerapp.R
import ge.nnasaridze.messengerapp.databinding.ConversationsRecyclerItemBinding
import ge.nnasaridze.messengerapp.scenes.menu.ConversationsRecyclerAdapter.ConversationsRecyclerViewHolder


class ConversationsRecyclerAdapter(private val handler: (position: Int) -> Unit) :
    RecyclerView.Adapter<ConversationsRecyclerViewHolder>() {
    var data: List<Int> = arrayListOf() //TODO

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

    override fun onBindViewHolder(holder: ConversationsRecyclerViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: ArrayList<Int>) {
        data = newData
    }

    class ConversationsRecyclerViewHolder(
        private val binding: ConversationsRecyclerItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Int) {
            with(binding) {
                conversationsName.text = "name"
                conversationsProf.text = "prof"
                conversationsTime.text = "12:10"
                conversationsIcon.setImageResource(R.drawable.avatar_image_placeholder)
            }
        }
    }
}