package in.woloo.www.dashboard.ui.thirstreminder;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.woloo.www.R;

public class ThirstReminderDialog extends DialogFragment {
    public static final String TAG = "ThirstReminderDialog";
    private ThirstCallbacks callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof ThirstCallbacks) {
            callback = (ThirstCallbacks) getActivity();
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentAnimation);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ask_thirstreminder, container);
        ButterKnife.bind(this, view);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    @OnClick(R.id.tvYes)
    protected void onClickYes(){
        if(callback != null){
            callback.onClickThirstYes();
        }
        dismiss();
    }

    @OnClick(R.id.tvNo)
    protected void onClickNo(){

        if(callback != null){
            callback.onClickThirstNo();
        }
        dismiss();
    }

    @OnClick(R.id.ivClose)
    protected void onClickClose(){
        dismiss();
    }

    public interface ThirstCallbacks {
        void onClickThirstYes();
        void onClickThirstNo();
    }
}
