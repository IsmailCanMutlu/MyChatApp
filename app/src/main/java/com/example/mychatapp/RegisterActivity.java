package com.example.mychatapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;





public class RegisterActivity extends Activity {

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();


        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                // Kullanıcı kaydı için Firebase metodunu kullan
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Kayıt başarılı, kullanıcı bilgilerini Firebase'e ekle
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userId = user.getUid();
                                    String username = editTextUsername.getText().toString().trim();
                                    User newUser = new User(username, email);
                                    newUser.setId(userId); // Kullanıcı ID'sini ayarla

                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://mychatapp-3fcae-default-rtdb.europe-west1.firebasedatabase.app").getReference("users");
                                    Log.d("RegisterActivity", "Firebase Auth successful. User ID: " + userId);
                                    databaseRef.child(userId).setValue(newUser)
                                            .addOnSuccessListener(aVoid -> Log.d("RegisterActivity", "User data saved successfully"))
                                            .addOnFailureListener(e -> Log.e("RegisterActivity", "Failed to save user data", e));
                                    // Kayıt başarılı, LoginActivity'ye yönlendir
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Eğer kayıt başarısız olursa, kullanıcıya bir hata mesajı göster
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
