package ge.nnasaridze.messengerapp.shared.repositories.authentication

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.utils.EMAIL_SUFFIX
import ge.nnasaridze.messengerapp.shared.utils.UNREGISTERED

interface AuthenticationRepository {
    fun getID(): String
    fun isAuthenticated(): Boolean
    fun signout()

    fun authenticate(
        nickname: String, password: String,
        handler: (isSuccessful: Boolean) -> Unit
    )

    fun register(
        nickname: String, password: String, profession: String,
        handler: (isSuccessful: Boolean) -> Unit
    )

    fun isValidCredential(credential: String): Boolean
}

class DefaultAuthenticationRepository : AuthenticationRepository {


    private val auth = Firebase.auth

    override fun getID(): String {
        return auth.uid ?: UNREGISTERED
    }

    override fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun authenticate(
        nickname: String,
        password: String,
        handler: (isSuccessful: Boolean) -> Unit
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
        profession: String,
        handler: (isSuccessful: Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword("$nickname$EMAIL_SUFFIX", password)
            .addOnCompleteListener { task ->
                handler(task.isSuccessful)
            }
        //TODO profession
    }

    override fun isValidCredential(credential: String): Boolean {
        return credential.isNotEmpty() &&
                credential.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' }
                    .length == credential.length
    }
}

