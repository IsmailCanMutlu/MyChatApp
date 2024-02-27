package com.example.mychatapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;




public class LoginActivity extends Activity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Kullanıcı girişi için Firebase metodunu kullan
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("LoginActivity", "Giriş başarılı, UserListActivity'ye yönlendiriliyor.");
                                    // Giriş başarılı, kullanıcı bilgilerini Firebase'den al
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Giriş başarılı, UserListActivity'ye yönlendir
                                    Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Eğer giriş başarısız olursa, kullanıcıya bir hata mesajı göster
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
