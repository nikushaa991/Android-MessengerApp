package ge.nnasaridze.messengerapp.shared.authenticator

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.EMAIL_SUFFIX
import ge.nnasaridze.messengerapp.shared.UNREGISTERED

class AuthenticatorImpl : Authenticator {
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

    override fun register(
        nickname: String,
        password: String,
        profession: String,
        handler: (isSuccessful: Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword("nickname$EMAIL_SUFFIX", password)
            .addOnCompleteListener { task -> handler(task.isSuccessful) }
        //TODO: profession
    }

}

