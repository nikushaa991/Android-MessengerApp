package ge.nnasaridze.messengerapp.shared.entities

data class MessageEntity(
    val sender: String? = null,
    val message: String? = null,
    val timestamp: Int? = null
)