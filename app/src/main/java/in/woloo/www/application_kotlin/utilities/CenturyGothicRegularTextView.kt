package `in`.woloo.www.application_kotlin.utilities

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CenturyGothicRegularTextView: AppCompatTextView {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init(attrs)
    }

    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    private fun init(attrs: AttributeSet?) {
        val tf = Typeface.createFromAsset(
            context.assets,
            "Century Gothic.ttf"
        )
        typeface = tf
    }
}