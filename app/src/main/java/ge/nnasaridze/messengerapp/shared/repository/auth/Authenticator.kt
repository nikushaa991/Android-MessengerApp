package ge.nnasaridze.messengerapp.shared.repository.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.EMAIL_SUFFIX
import ge.nnasaridze.messengerapp.shared.UNREGISTERED
import kotlinx.coroutines.CompletableDeferred

class Authenticator {
    private val auth = Firebase.auth

    fun getID(): String {
        return auth.uid ?: UNREGISTERED
    }

    fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    fun authenticate(
        nickname: String,
        password: String,
        handler: (isSuccessful: Boolean) -> Unit
    ) {
        auth.signInWithEmailAndPassword("$nickname$EMAIL_SUFFIX", password)
            .addOnCompleteListener { task -> handler(task.isSuccessful) }
    }


    fun register(
        nickname: String,
        password: String,
        handler: (isSuccessful: Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword("$nickname$EMAIL_SUFFIX", password)
            .addOnCompleteListener { task ->
                handler(task.isSuccessful)
            }
    }


}

