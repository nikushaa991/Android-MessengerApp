package ge.nnasaridze.messengerapp.shared.authenticator

interface Authenticator {
    fun getID(): String
    fun isAuthenticated(): Boolean
    fun authenticate(nickname: String, password: String, handler: (isSuccessful: Boolean) -> Unit)

    fun register(
        nickname: String,
        password: String,
        profession: String,
        handler: (isSuccessful: Boolean) -> Unit
    )

    companion object {
        private var instance = AuthenticatorImpl()

        fun getInstance(): Authenticator {
            return instance
        }
    }
}