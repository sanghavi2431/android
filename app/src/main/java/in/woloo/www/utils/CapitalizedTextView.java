package in.woloo.www.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;


public class CapitalizedTextView extends AppCompatTextView {

    public CapitalizedTextView(@NonNull Context context) {
        super(context);
    }

    public CapitalizedTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CapitalizedTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
        public void setText(CharSequence text, BufferType type) {
            StringBuilder builder = new StringBuilder(text);
            builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));
            super.setText(builder.toString(), type);
        }

}
