package ge.nnasaridze.messengerapp.shared.entities


data class ChatEntity(
    var chatID: String,
) {
    lateinit var user: UserEntity
    lateinit var lastMessage: MessageEntity

    fun userIsInitialized() = ::user.isInitialized
    fun messageIsInitialized() = ::lastMessage.isInitialized
}
