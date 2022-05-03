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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getName();

    private EditText emailEt;
    private EditText passwordEt;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.loginEditTextEmail);
        passwordEt = findViewById(R.id.loginEditTextPassword);
        mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();
    }

    public void onLogin(View view) {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        Log.i(LOG_TAG, "Bejelentkezett: " + email + "\tJelszava: " + password);

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Tötlsön ki minden mezőt!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Log.d(LOG_TAG, "Sikeres bejelentkezés!");
                startMain();
            }
            else {
                Log.d(LOG_TAG, "Sikertelen bejelentkezés!");
                Toast.makeText(LoginActivity.this, "Sikertelen bejelentkezés: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onLoginAsGuest(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Log.d(LOG_TAG, "Sikeres anonim bejelentkezés!");
                startMain();
            }
            else {
                Log.d(LOG_TAG, "Sikertelen anonim bejelentkezés!");
                Toast.makeText(LoginActivity.this, "Sikertelen anonim bejelentkezés: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onRegistration(View view) {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        Intent intent = new Intent(this, RegistrationActivity.class);

        intent.putExtra("email", email);
        intent.putExtra("password", password);
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