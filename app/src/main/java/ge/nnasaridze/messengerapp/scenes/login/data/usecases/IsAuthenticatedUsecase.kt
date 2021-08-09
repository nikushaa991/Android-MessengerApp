package ge.nnasaridze.messengerapp.scenes.login.data.usecases

import ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication.DefaultAuthenticationRepository

interface IsAuthenticatedUsecase {
    fun execute(
        onCompleteHandler: (isAuthenticated: Boolean) -> Unit,
    )
}

class DefaultIsAuthenticatedUsecase : IsAuthenticatedUsecase {


    private val repo = DefaultAuthenticationRepository()

    override fun execute(
        onCompleteHandler: (isAuthenticated: Boolean) -> Unit,
    ) {
        onCompleteHandler(repo.isAuthenticated())
    }
}
