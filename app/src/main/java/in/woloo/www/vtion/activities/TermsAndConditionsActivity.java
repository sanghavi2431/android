package in.woloo.www.vtion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import in.woloo.www.R;
import in.woloo.www.SelectGender.SelectGenderActivity;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.databinding.ActivityTermsAndConditionsBinding;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.WebActivity;
import in.woloo.www.v2.data.local.SharedPrefSettings;

public class TermsAndConditionsActivity extends AppCompatActivity {

    ActivityTermsAndConditionsBinding binding;
    private static final int ACCESSIBILITY_REQUEST_CODE = 101 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermsAndConditionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.checkAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // If CheckBox is checked, enable the TextView
                binding.tvAgree.setEnabled(true);
            } else {
                // If CheckBox is unchecked, disable the TextView
                binding.tvAgree.setEnabled(false);
            }
        });

        binding.termsTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra("privacy_policy","https://woloo.in/privacy-policy/");
                startActivity(intent);
            }
        });

        binding.termsFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra("terms_conditions","https://woloo.in/terms-condition/");
                startActivity(intent);
            }
        });

        binding.tvDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGenderActivity();
            }
        });

        binding.tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConsentDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case ACCESSIBILITY_REQUEST_CODE:
                // Check again if accessibility service is enabled
                if (isAccessibilityServiceEnabled()) {
                    // Accessibility service is enabled now, launch vtion activity

                    callVitionActivity();
                } else {
                    // Accessibility service is still not enabled, launch normal flow gender activity
                    callGenderActivity();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    // Check if accessibility service is enabled
    private boolean isAccessibilityServiceEnabled() {
        int accessibilityEnabled = Settings.Secure.getInt(
                getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED,
                0
        );
        return accessibilityEnabled == 1;
    }

    // Call VitionUserDataActivity
    private void callVitionActivity() {

        Intent intent = new Intent(this, VitionUserDataActivity.class);
        startActivity(intent);
        finish();
        SharedPrefSettings sharedPrefSettings = new SharedPrefSettings();
        sharedPrefSettings.storeIsVTIONUser(true);
    }

    // Call SelectGenderActivity
    private void callGenderActivity() {
        Intent intent = new Intent(this, SelectGenderActivity.class);
        startActivity(intent);
        finish();
    }

    private void callAccessibility()
    {
        Logger.i("TAG", "clicked");
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, ACCESSIBILITY_REQUEST_CODE);
    }


    private void showConsentDialog() {
        Logger.i("TAG", "showLogoutDialog");
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TermsAndConditionsActivity.this);
            View child = getLayoutInflater().inflate(R.layout.dialog_consent_layout, null);
            alertDialogBuilder.setView(child);
            alertDialogBuilder.setCancelable(false);
            AlertDialog alertDialog = alertDialogBuilder.create();
            TextView tvDeny = child.findViewById(R.id.tv_disagree_dialog);
            TextView tvAgree = child.findViewById(R.id.tv_agree_dialog);



            // Set fullscreen layout parameters
            alertDialog.setOnShowListener(dialogInterface -> {
                Window window = alertDialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.gravity = Gravity.BOTTOM; // Position the dialog at the bottom
                    window.setAttributes(params);
                }
            });

            tvDeny.setOnClickListener(v -> {
                alertDialog.dismiss();
                callGenderActivity();
            });
            tvAgree.setOnClickListener(v -> {

                    callAccessibility();
                alertDialog.dismiss();
            });
            alertDialog.show();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }


}