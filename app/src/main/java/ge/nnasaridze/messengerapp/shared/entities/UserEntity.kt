package ge.nnasaridze.messengerapp.shared.entities

data class UserEntity(
    val nickname: String? = null,
    val profession: String? = null,
    val chatIDs: List<String>? = null,
    val imageID: Int? = null,
)