package ge.nnasaridze.messengerapp.shared.data.api.dtos

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

@IgnoreExtraProperties
data class UserDTO(
    val nickname: String? = null,
    val profession: String? = null,
    val chatIDs: Map<String, String>? = null,
) {
    @Exclude
    fun toEntity(id: String): UserEntity {
        return UserEntity(
            id,
            nickname?:"",
            profession?:"",
        )
    }
}