package ge.nnasaridze.messengerapp.shared.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ge.nnasaridze.messengerapp.R

interface ImageLoader {
    fun loadImage(context: Context, image: Uri, view: ImageView)
    fun loadImage(context: Fragment, image: Uri, view: ImageView)
}

class DefaultImageLoader: ImageLoader {
    override fun loadImage(context: Context, image: Uri, view: ImageView){
        Glide.with(context)
            .load(image)
            .placeholder(R.drawable.avatar_image_placeholder)
            .error(R.drawable.avatar_image_placeholder)
            .into(view)
    }

    override fun loadImage(context: Fragment, image: Uri, view: ImageView){
        Glide.with(context)
            .load(image)
            .placeholder(R.drawable.avatar_image_placeholder)
            .error(R.drawable.avatar_image_placeholder)
            .into(view)
    }
}