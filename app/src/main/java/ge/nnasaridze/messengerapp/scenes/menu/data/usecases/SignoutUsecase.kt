package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.scenes.menu.data.repositories.AuthRepository

interface SignoutUsecase {
    fun execute()
}

class DefaultSignoutUsecase : SignoutUsecase{


    private val repo = AuthRepository()

    override fun execute() {
        repo.signout()
    }
}