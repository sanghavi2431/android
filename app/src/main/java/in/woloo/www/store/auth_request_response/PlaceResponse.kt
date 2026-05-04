package `in`.woloo.www.store.auth_request_response

data class State(
    val id: Int,
    val name: String,
    val country_id: Int
)

data class City(
    val id: Int,
    val name: String,
    val state_id: Int
)

