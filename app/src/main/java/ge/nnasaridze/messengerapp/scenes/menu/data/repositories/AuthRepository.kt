package ge.nnasaridze.messengerapp.scenes.menu.data.repositories

import ge.nnasaridze.messengerapp.shared.repositories.Repository

class AuthRepository {

    val repo = Repository.getInstance()

    fun signout() {
        repo.signout()
    }
}