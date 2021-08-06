package ge.nnasaridze.messengerapp.shared.data.entities


data class MessageEntity(
    var senderID: String,
    var text: String,
    var timestamp: Long,
)