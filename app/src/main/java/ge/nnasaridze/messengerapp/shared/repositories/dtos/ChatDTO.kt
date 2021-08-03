package ge.nnasaridze.messengerapp.shared.repositories.dtos

import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.entities.ChatEntity

@IgnoreExtraProperties
data class ChatDTO(
    val user: UserDTO? = null,
    val lastMessage: MessageDTO? = null,
) {
    fun toEntity(chatID: String): ChatEntity {
        return ChatEntity(
            chatID,
            user?.toEntity(),
            lastMessage?.toEntity(),
        )
    }
}

