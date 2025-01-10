package `in`.woloo.www.more.giftcard.model


class GiftCardModelResponse(@kotlin.jvm.JvmField val data: Data) {
    class Data(
        val status: String,
        val message: String,
        val giftRecieved: GiftRecieved,
        val giftSent: GiftSent
    ) {
        class GiftRecieved(
            val userId: Int,
            val transactionType: String,
            val remarks: String,
            val value: Int,
            val type: String,
            val isGift: Int,
            val updatedAt: String,
            val createdAt: String,
            val id: Int
        )

        class GiftSent(
            val userId: Int,
            val transactionType: String,
            val remarks: String,
            val value: Int,
            val type: String,
            val isGift: Int,
            val updatedAt: String,
            val createdAt: String,
            val id: Int
        )
    }
}
