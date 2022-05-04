package hu.mobilalk.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegistrationActivity.class.getName();

    EditText usernameEt;
    EditText passwordEt;
    EditText passwordAgainEt;
    EditText emailEt;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Log.i(LOG_TAG, "pedig sikerül");

        usernameEt = findViewById(R.id.registrationEditTextUsername);
        passwordEt = findViewById(R.id.registrationEditTextPass);
        passwordAgainEt = findViewById(R.id.registrationEditTextPassAgain);
        emailEt = findViewById(R.id.registrationEditTextEmail);

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        emailEt.setText(email);
        passwordEt.setText(password);
        passwordAgainEt.setText(password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void onRegistrating(View view) {
        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String passwordAgain = passwordAgainEt.getText().toString();
        String email = emailEt.getText().toString();

        String str = "Regisztrált: " + username + ", jelszava: " + (password.equals(passwordAgain) ? password + ", email: " :
                "a két jelszó nem egyezik meg, email: ") + email;

        Log.i(LOG_TAG, str);

        if(password.equals(passwordAgain)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()) {
                    startMain();
                }
                else {
                    Log.d(LOG_TAG, "Sikertelen regisztráció!");
                    Toast.makeText(RegistrationActivity.this, "Sikertelen regisztráció!: " + task.getException().getMessage(),  Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(RegistrationActivity.this, "Sikertelen regisztráció: A jelszó nem egyezik meg a jelszó megerősítéssel!", Toast.LENGTH_LONG).show();
        }
    }

    public void onCancel(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}