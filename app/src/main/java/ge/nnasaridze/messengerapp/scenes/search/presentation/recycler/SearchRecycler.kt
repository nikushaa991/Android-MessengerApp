package ge.nnasaridze.messengerapp.scenes.search.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nnasaridze.messengerapp.databinding.SearchRecyclerItemBinding
import ge.nnasaridze.messengerapp.shared.utils.DefaultImageLoader
import ge.nnasaridze.messengerapp.shared.utils.ImageLoader


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
        notifyDataSetChanged()
    }

    class SearchRecyclerViewHolder(
        private val binding: SearchRecyclerItemBinding,
        private val imageLoader: ImageLoader = DefaultImageLoader(),
        ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: RecyclerUserEntity) {
            with(binding) {
                searchName.text = data.nickname
                searchProf.text = data.profession

                imageLoader.loadImage(itemView.context, data.image, searchIcon)
            }
        }
    }
}