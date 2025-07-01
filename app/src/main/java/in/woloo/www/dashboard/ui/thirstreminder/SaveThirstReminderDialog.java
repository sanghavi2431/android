package in.woloo.www.dashboard.ui.thirstreminder;

import android.animation.Animator;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.utils.Logger;

public class SaveThirstReminderDialog extends DialogFragment {
    public static final String TAG = "SaveThirstReminderDialog";
    private SaveThirstCallbacks callback;
    private int hours;

    @BindView(R.id.tvThirstTimerMessage)
    TextView tvThirstTimerMessage;

    @BindView(R.id.etThirstFrequency)
    EditText etThirstFrequency;

    @BindView(R.id.cbCancelReminder)
    CheckBox cbCancelReminder;

    public SaveThirstReminderDialog(int hours) {
        this.hours = hours;
    }

    public SaveThirstReminderDialog() {
        this.hours = 5;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof SaveThirstCallbacks) {
            callback = (SaveThirstCallbacks) getActivity();
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentAnimation);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_set_thirstreminder, container);
        ButterKnife.bind(this, view);
        etThirstFrequency.setText(String.valueOf(hours));
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @OnClick(R.id.tvSave)
    protected void onClickSave() {
        if (callback != null) {
            if (cbCancelReminder.isChecked()) {
                hours = 0;
            }
            callback.onClickSaveThirstReminder(hours);
        }
        dismiss();
    }

    @OnClick(R.id.ivClose)
    protected void onClickClose() {
        dismiss();
    }

    @OnTextChanged(value = R.id.etThirstFrequency, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onFrequencyChanged(CharSequence text) {
        Logger.i(TAG, text.toString());
        try {
            hours = (text == null || TextUtils.isEmpty(text)) ? 0 : Integer.parseInt(text.toString());
        }catch(Exception e){
            new CommonUtils().printStackTrace(e);
        }
        tvThirstTimerMessage.setText(getString(R.string.thirst_reminder_confirmation_value, hours));
    }

    public interface SaveThirstCallbacks {
        void onClickSaveThirstReminder(int hours);

        //void onClickThirstNo();
    }
}
