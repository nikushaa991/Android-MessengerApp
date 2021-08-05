package ge.nnasaridze.messengerapp.scenes.chat.presentation

import ge.nnasaridze.messengerapp.scenes.chat.data.usecases.DefaultGetMessagesUsecase
import ge.nnasaridze.messengerapp.scenes.chat.data.usecases.GetMessagesUsecase

class ChatPresenterImpl(
    private val view: ChatView,
    private val chatID: String,
    private val getMessagesUsecase: GetMessagesUsecase = DefaultGetMessagesUsecase(),
) : ChatPresenter {

    override fun viewInitialized() {
        TODO("Not yet implemented")
    }

    override fun sendPressed(text: String) {
        view.emptyText()

    }

    override fun backPressed() {
        view.gotoMenu()
    }


}