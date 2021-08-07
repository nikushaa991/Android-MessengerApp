package ge.nnasaridze.messengerapp.shared.data.entities


data class ChatEntity(
    var chatID: String,
    var user: UserEntity,
    var lastMessage: MessageEntity,
)
