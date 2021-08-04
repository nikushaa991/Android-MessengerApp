package ge.nnasaridze.messengerapp.shared.entities


data class MessageEntity(
    var messageID: String,
    var senderID: String,
    var text: String,
    var timestamp: Int
)