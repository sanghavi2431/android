package `in`.woloo.www.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CapitalizedTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setText(text: CharSequence, type: BufferType) {
        val builder = StringBuilder(text)
        builder.setCharAt(0, builder[0].uppercaseChar())
        super.setText(builder.toString(), type)
    }
}
