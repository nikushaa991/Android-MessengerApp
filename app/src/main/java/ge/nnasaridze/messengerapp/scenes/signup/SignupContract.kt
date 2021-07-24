package ge.nnasaridze.messengerapp.scenes.signup

interface SignupView{
    fun displayError(text: String)

    fun getNickname() : String
    fun getPassword() : String
    fun getProfession() : String

    fun gotoMenu()

    fun showLoading()
    fun hideLoading()
}

interface SignupPresenter{
    fun signupPressed()
}