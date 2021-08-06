package ge.nnasaridze.messengerapp.shared.data.repositories.dtos

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.data.entities.ChatEntity

@IgnoreExtraProperties
data class ChatDTO(
    val userID: String,
    val lastMessageID: String? = null,
) {
    @Exclude
    fun toEntity(chatID: String): ChatEntity {
        return ChatEntity(chatID = chatID)
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userID" to userID,
            "lastMessageID" to lastMessageID,
        )
    }
}

