package ge.nnasaridze.messengerapp.scenes.search.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.nnasaridze.messengerapp.R
import ge.nnasaridze.messengerapp.databinding.SearchRecyclerItemBinding


class SearchRecyclerAdapter(private val handler: (position: Int) -> Unit) :
    RecyclerView.Adapter<SearchRecyclerAdapter.SearchRecyclerViewHolder>() {


    private var data: List<RecyclerUserEntity> = arrayListOf()

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

    fun setData(newData: List<RecyclerUserEntity>) {
        data = newData
    }

    class SearchRecyclerViewHolder(
        private val binding: SearchRecyclerItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: RecyclerUserEntity) {
            with(binding) {
                searchName.text = data.nickname
                searchProf.text = data.profession

                Glide.with(itemView.context)
                    .load(data.image)
                    .placeholder(R.drawable.avatar_image_placeholder)
                    .error(R.drawable.avatar_image_placeholder)
                    .into(searchIcon)
            }
        }
    }
}