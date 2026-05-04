package `in`.woloo.www.dashboard.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import `in`.woloo.www.R
import `in`.woloo.www.utils.Logger.i

class ShopFragment : Fragment() {
    private var shopViewModel: ShopViewModel? = null

    /*calling on onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        i(TAG, "onCreateView")
        shopViewModel =
            ViewModelProvider(this).get(ShopViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shop, container, false)
        shopViewModel!!.text.observe(
            viewLifecycleOwner
        ) { }

        return root
    }

    companion object {
        var TAG: String = ShopFragment::class.java.simpleName
    }
}