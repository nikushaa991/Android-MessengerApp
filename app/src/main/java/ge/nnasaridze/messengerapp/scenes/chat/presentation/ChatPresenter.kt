package ge.nnasaridze.messengerapp.scenes.chat.presentation

import ge.nnasaridze.messengerapp.scenes.chat.data.usecases.*
import ge.nnasaridze.messengerapp.scenes.chat.presentation.recycler.RecyclerMessageEntity
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

class ChatPresenterImpl(
    private val view: ChatView,
    private val chatID: String,
    private val recipientID: String,
    private val subscribeMessagesUsecase: SubscribeMessagesUsecase = DefaultSubscribeMessagesUsecase(),
    private val subscribeUserUsecase: SubscribeUserUsecase = DefaultSubscribeUserUsecase(),
    private val sendMessageUsecase: SendMessageUsecase = DefaultSendMessageUsecase(),
) : ChatPresenter {


    private val data = mutableListOf<RecyclerMessageEntity>()
    private lateinit var recipient: UserEntity

    override fun viewInitialized() {
        view.showLoading()
        loadMessages()
        subscribeUserUsecase.execute(
            userID = recipientID,
            onSuccessHandler = ::recipientHandler,
            errorHandler = ::errorHandler)
    }

    override fun sendPressed() {
        val text = view.getText()
        if (text == "")
            return
        view.emptyText()
        sendMessageUsecase.execute(
            chatID = chatID,
            text = text,
            successHandler = {},
            errorHandler = ::errorHandler)
    }

    override fun backPressed() {
        view.closeChat()
    }

    private fun refreshViewData() {
        view.updateChat(data)
    }

    private fun loadMessages() {
        subscribeMessagesUsecase.execute(
            chatID = chatID,
            newMessageHandler = ::newMessageHandler,
            errorHandler = ::errorHandler)
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

    private fun errorHandler(text: String) {
        view.displayError(text)
    }

}