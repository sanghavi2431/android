package `in`.woloo.www.more.fragments.mvp

interface InviteFriendsView {
    fun inviteFriendSuccess(msg: String?)
    fun showRefferalCode(refCode: String?, expiryDate: String?)
}
