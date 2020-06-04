package neue.project.invisiblegallery.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import neue.project.invisiblegallery.R;

public class AuthenticationActivity extends SecureActivity {
    public static final String AUTH_SET = "auth_set";
    public static final String AUTH_PASS = "auth_pass";

    private TextView authTitleText, fpWarningText;
    private Button skipAuthRegisterButton;
    private ImageButton confirmPasswordButton;
    private FloatingActionButton fingerprintButton;
    private EditText passwordEdit;

    private String password;

    private SharedPreferences pref;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_authentication);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        init();
        authenticateOrPass();
    }

    private void init() {
        authTitleText = findViewById(R.id.text_auth_title);
        fpWarningText = findViewById(R.id.text_fp_warning);
        skipAuthRegisterButton = findViewById(R.id.button_skip_auth_register);
        confirmPasswordButton = findViewById(R.id.button_confirm_password);
        fingerprintButton = findViewById(R.id.button_fingerprint);
        passwordEdit = findViewById(R.id.edit_password);

        skipAuthRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref
                        .edit()
                        .putBoolean(AUTH_SET, false)
                        .apply();
                pass();
            }
        });
    }

    private void authenticateOrPass() {
        if (pref.contains(AUTH_SET)){
            if (pref.getBoolean(AUTH_SET, false)){
                auth();
            } else {
                pass();
            }
        } else {
            setAuth();
        }
    }

    private void auth() {
        authTitleText.setText(R.string.auth_required_title);
        passwordEdit.setHint(R.string.enter_password);
        skipAuthRegisterButton.setVisibility(View.GONE);

        password = pref.getString(AUTH_PASS, "");

        confirmPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEdit.getText().toString().equals(password)) {
                    pass();
                } else {
                    passwordEdit.clearComposingText();
                    Snackbar.make(v, R.string.incorrect_password, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setAuth() {
        passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        confirmPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref
                        .edit()
                        .putString(AUTH_PASS, passwordEdit.getText().toString())
                        .putBoolean(AUTH_SET, true)
                        .apply();

                pass();
            }
        });
    }

    private void pass() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
