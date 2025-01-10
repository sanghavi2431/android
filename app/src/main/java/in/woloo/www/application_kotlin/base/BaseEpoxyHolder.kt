package `in`.woloo.www.application_kotlin.base

import android.view.View
import androidx.annotation.CallSuper
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyHolder

abstract class BaseEpoxyHolder : EpoxyHolder() {
    @CallSuper
    override fun bindView(itemView: View) {
        ButterKnife.bind(this, itemView)
    }
}