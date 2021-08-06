package ge.nnasaridze.messengerapp.scenes.chat.presentation

import ge.nnasaridze.messengerapp.scenes.chat.data.usecases.DefaultGetMessagesUsecase
import ge.nnasaridze.messengerapp.scenes.chat.data.usecases.DefaultSendMessageUsecase
import ge.nnasaridze.messengerapp.scenes.chat.data.usecases.GetMessagesUsecase
import ge.nnasaridze.messengerapp.scenes.chat.data.usecases.SendMessageUsecase
import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.RecyclerMessageEntity
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.usecases.DefaultGetUserUsecase
import ge.nnasaridze.messengerapp.shared.data.usecases.GetUserUsecase

class ChatPresenterImpl(
    private val view: ChatView,
    private val chatID: String,
    private val recipientID: String,
    private val getMessagesUsecase: GetMessagesUsecase = DefaultGetMessagesUsecase(),
    private val getUserUsecase: GetUserUsecase = DefaultGetUserUsecase(),
    private val sendMessageUsecase: SendMessageUsecase = DefaultSendMessageUsecase(),
) : ChatPresenter {


    private val data = mutableListOf<RecyclerMessageEntity>()
    private lateinit var recipient: UserEntity

    override fun viewInitialized() {
        view.showLoading()
        loadMessages()
        getUserUsecase.execute(recipientID, ::recipientHandler)
    }

    override fun sendPressed() {
        val text = view.getText()
        view.emptyText()
        sendMessageUsecase.execute(chatID, text, ::sendHandler)
    }

    override fun backPressed() {
        view.closeChat()
    }

    private fun refreshViewData() {
        view.updateChat(data)
    }

    private fun loadMessages() {
        getMessagesUsecase.execute(chatID, ::newMessageHandler)
    }

    private fun newMessageHandler(message: RecyclerMessageEntity) {
        data.add(message)
        data.sortByDescending { message.timestamp }
        refreshViewData()
    }

    private fun recipientHandler(user: UserEntity) {
        recipient = user
        view.setTitle(recipient.nickname)
        view.hideLoading()
    }

    private fun sendHandler(isSuccessful: Boolean) {

    }

}