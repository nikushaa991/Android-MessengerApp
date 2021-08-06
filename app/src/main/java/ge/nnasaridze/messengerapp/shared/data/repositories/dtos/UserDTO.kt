package ge.nnasaridze.messengerapp.shared.data.repositories.dtos

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

@IgnoreExtraProperties
data class UserDTO(
    val nickname: String,
    val profession: String,
    val chatIDs: List<String>? = null,
) {
    @Exclude
    fun toEntity(id: String): UserEntity {
        return UserEntity(
            id,
            nickname,
            profession,
        )
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nickname" to nickname,
            "profession" to profession,
            "chatIDs" to chatIDs,
        )
    }
}