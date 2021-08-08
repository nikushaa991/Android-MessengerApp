package ge.nnasaridze.messengerapp.scenes.search.presentation.recycler

import android.net.Uri

data class RecyclerUserEntity(
    var userID: String,
    var nickname: String,
    var profession: String,
    var image: Uri,
)
