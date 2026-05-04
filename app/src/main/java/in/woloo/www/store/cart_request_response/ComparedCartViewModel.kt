package `in`.woloo.www.store.cart_request_response

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ComparedCartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<ComparedItemsDataForCart>>()
    val cartItems: LiveData<List<ComparedItemsDataForCart>> = _cartItems

    fun updateCartItems(newList: List<ComparedItemsDataForCart>) {
        _cartItems.value = newList
    }
}