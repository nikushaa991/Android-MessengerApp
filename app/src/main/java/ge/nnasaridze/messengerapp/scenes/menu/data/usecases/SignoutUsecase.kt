package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.DefaultAuthenticationRepository

interface SignoutUsecase {
    fun execute()
}

class DefaultSignoutUsecase : SignoutUsecase {
    private val repo = DefaultAuthenticationRepository()
    override fun execute() {
        repo.signout()
    }
}