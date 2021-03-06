package ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface AuthenticationRepository {
    fun getID(): String
    fun isAuthenticated(): Boolean
    fun signout()

    fun authenticate(
        nickname: String, password: String,
        handler: (isSuccessful: Boolean) -> Unit,
    )

    fun register(
        nickname: String, password: String,
        handler: (isSuccessful: Boolean) -> Unit,
    )

    fun updateName(
        nickname: String,
        handler: (isSuccessful: Boolean) -> Unit,
    )

    fun isValidCredential(credential: String): Boolean
    fun isValidPassword(password: String): Boolean
}

class DefaultAuthenticationRepository : AuthenticationRepository {


    private val auth = Firebase.auth

    override fun getID(): String {
        return auth.uid ?: ""
    }

    override fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun authenticate(
        nickname: String,
        password: String,
        handler: (isSuccessful: Boolean) -> Unit,
    ) {
        auth.signInWithEmailAndPassword("$nickname$EMAIL_SUFFIX", password)
            .addOnCompleteListener { task -> handler(task.isSuccessful) }
    }

    override fun signout() {
        auth.signOut()
    }

    override fun register(
        nickname: String,
        password: String,
        handler: (isSuccessful: Boolean) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword("$nickname$EMAIL_SUFFIX", password)
            .addOnCompleteListener { task ->
                handler(task.isSuccessful)
            }
    }

    override fun updateName(nickname: String, handler: (isSuccessful: Boolean) -> Unit) {
        auth.currentUser!!.updateEmail("$nickname$EMAIL_SUFFIX").addOnCompleteListener { task ->
            handler(task.isSuccessful)
        }
    }

    override fun isValidCredential(credential: String): Boolean {
        return credential.isNotEmpty() &&
                credential.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' }
                    .length == credential.length
    }

    override fun isValidPassword(password: String): Boolean {
        return password.length > 6 &&
                password.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' }
                    .length == password.length
    }

    companion object {
        const val EMAIL_SUFFIX = "@fake.fake"
    }
}

