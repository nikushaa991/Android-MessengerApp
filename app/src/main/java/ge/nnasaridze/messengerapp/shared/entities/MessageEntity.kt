package ge.nnasaridze.messengerapp.shared.entities


data class MessageEntity(
    var senderID: String,
    var text: String,
    var timestamp: Int
)