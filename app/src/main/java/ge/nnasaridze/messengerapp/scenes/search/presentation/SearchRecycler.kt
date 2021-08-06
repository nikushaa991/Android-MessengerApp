package ge.nnasaridze.messengerapp.scenes.search.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nnasaridze.messengerapp.databinding.SearchRecyclerItemBinding
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity


class SearchRecyclerAdapter(private val handler: (position: Int) -> Unit) :
    RecyclerView.Adapter<SearchRecyclerAdapter.SearchRecyclerViewHolder>() {


    private var data: List<UserEntity> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchRecyclerViewHolder {
        val vh = SearchRecyclerViewHolder(
            SearchRecyclerItemBinding.inflate(
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
        holder: SearchRecyclerViewHolder,
        position: Int,
    ) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<UserEntity>) {
        data = newData
    }

    class SearchRecyclerViewHolder(
        private val binding: SearchRecyclerItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: UserEntity) {
            with(binding) {
                searchName.text = data.nickname
                searchProf.text = data.profession

                //TODO get image
            }
        }
    }
}