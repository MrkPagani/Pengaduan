package com.example.pengaduan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText edtUsername,edtPassword,edtNama,edtNik,edtTelp;
    Button btnSignup;
    DatabaseReference db,ref,reference;
    private FirebaseAuth mAuth;
    String nama,username,nik,telp,level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtNama = findViewById(R.id.nama);
        edtNik = findViewById(R.id.nik);
        edtTelp = findViewById(R.id.telp);
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        btnSignup = findViewById(R.id.btnSubmit);
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("masyarakat");
        reference = FirebaseDatabase.getInstance().getReference("petugas");
        db = FirebaseDatabase.getInstance().getReference("user");
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        final String user = edtUsername.getText().toString() + "@gmail.com";
        final String iNama = edtNama.getText().toString();
        final String iNik = edtNik.getText().toString();
        final String iTelp = edtTelp.getText().toString();
        final String password = edtPassword.getText().toString();
        if (user.isEmpty()){
            edtUsername.setError("Tolong masukan username");
            edtUsername.requestFocus();
        } else if (password.isEmpty()){
            edtPassword.setError("Tolong masukan password");
            edtPassword.requestFocus();
        } else if (iNama.isEmpty()){
            edtNama.setError("Tolong masukan nama");
            edtNama.requestFocus();
        } else if (iNik.isEmpty()){
            edtNik.setError("Tolong masukan nik");
            edtNik.requestFocus();
        } else if (iTelp.isEmpty()){
            edtTelp.setError("Tolong masukan No Telepon");
            edtTelp.requestFocus();
        } else if (user.isEmpty() && iNama.isEmpty() && password.isEmpty() && iNik.isEmpty() && iTelp.isEmpty()){
            Toast.makeText(Register.this, "Harap isi form untuk register", Toast.LENGTH_SHORT).show();
        } else if (!(user.isEmpty() && iNama.isEmpty() && password.isEmpty() && iNik.isEmpty() && iTelp.isEmpty())){
            mAuth.createUserWithEmailAndPassword(user,password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(Register.this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent toHome = new Intent(Register.this,Home.class);
                        startActivity(toHome);
                        finish();
                        nama = iNama;
                        username = user;
                        nik = iNik;
                        telp = iTelp;
                        if (user.equals("admin@gmail.com")){
                            level = "3";
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Petugas petugas = new Petugas(uid,nama,username,telp,level);
                            User user = new User(uid,nama,username,nik,telp,level);
                            db.child(uid).setValue(user);
                            reference.child(uid).setValue(petugas);
                        }else if(nik.equals("0")){
                            level = "2";
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Petugas petugas = new Petugas(uid,nama,username,telp,level);
                            User user = new User(uid,nama,username,nik,telp,level);
                            db.child(uid).setValue(user);
                            reference.child(uid).setValue(petugas);
                        } else{
                            level = "1";
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Masyarakat masyarakat = new Masyarakat(uid,nik,nama,username,password,telp,level);
                            User user = new User(uid,nama,username,nik,telp,level);
                            db.child(uid).setValue(user);
                            ref.child(uid).setValue(masyarakat);
                        }
                    }
                }
            });
        } else{
            Toast.makeText(Register.this, "Error Djantjok", Toast.LENGTH_SHORT).show();
        }
    }
}
