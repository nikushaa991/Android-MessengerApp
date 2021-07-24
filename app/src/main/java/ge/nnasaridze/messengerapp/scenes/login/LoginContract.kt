package ge.nnasaridze.messengerapp.scenes.login

interface LoginView{
    fun displayError(text: String)

    fun getNickname() : String
    fun getPassword() : String

    fun gotoSignup()
    fun gotoMenu()

    fun showLoading()
    fun hideLoading()
}

interface LoginPresenter{
    fun signinPressed()
    fun signupPressed()
}