package ge.nnasaridze.messengerapp.shared.entities


data class ChatEntity(
    val chatID: String? = null,
    val user: UserEntity? = null,
    val lastMessage: MessageEntity? = null,
)
