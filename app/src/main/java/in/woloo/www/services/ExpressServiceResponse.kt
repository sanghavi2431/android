package `in`.woloo.www.services

class ExpressServiceResponse {
}


data class ExpressServiceItem(
    val title: String,
    val imageResId: Int ,
    val serviceImage : Int
)

data class TakeSneakPeekServiceItem(
    val imageResId: String,
    val categori_id: String
)