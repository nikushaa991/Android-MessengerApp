package ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler

data class RecyclerMessageEntity(
    var text: String,
    var timestamp: Int,
    var sent: Boolean,
)