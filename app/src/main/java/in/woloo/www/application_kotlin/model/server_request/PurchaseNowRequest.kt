package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.annotations.SerializedName

data class PurchaseNowRequest (
    @SerializedName("powder_room_id")
    val powderRoomId: Int,

    @SerializedName("amount")
    val amountOfPowderRoom: Int
)