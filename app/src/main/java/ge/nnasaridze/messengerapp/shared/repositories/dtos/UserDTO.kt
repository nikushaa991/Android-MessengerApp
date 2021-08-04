package ge.nnasaridze.messengerapp.shared.repositories.dtos

import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.entities.UserEntity

@IgnoreExtraProperties
data class UserDTO(
    val nickname: String,
    val profession: String,
    val chatIDs: List<String>? = null,
) {
    fun toEntity(id: String): UserEntity {
        return UserEntity(
            id,
            nickname,
            profession,
        )
    }
}