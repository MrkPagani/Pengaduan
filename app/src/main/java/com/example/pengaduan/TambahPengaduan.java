package com.example.pengaduan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TambahPengaduan extends AppCompatActivity {

    DatabaseReference db,ref;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView btnBack;
    Uri filePath;
    TextView txtImage;
    EditText Edtnama,Edtisi,Edtnik,Edttgl,EdtSubject;
    String subject,nama,isi,nik,tanggal,img,status;
    Button btnSubmit,btnUpload,btnUpdate;
    Pengaduan pengaduans;
    final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengaduan);
        pengaduans = getIntent().getParcelableExtra("PENGADUAN");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String userId = currentUser.getUid();
        db = FirebaseDatabase.getInstance().getReference("pengaduan");
        ref = FirebaseDatabase.getInstance().getReference("user").child(userId);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        txtImage = findViewById(R.id.textImage);
        Edtnama = findViewById(R.id.nama);
        Edtisi = findViewById(R.id.isiLaporan);
        Edtnik = findViewById(R.id.nik);
        Edttgl = findViewById(R.id.tanggal);
        EdtSubject = findViewById(R.id.subject);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.back);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpload = findViewById(R.id.button);
        btnUpdate.setVisibility(View.GONE);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String getNama = dataSnapshot.child("nama").getValue(String.class);
                        String getNik = dataSnapshot.child("nik").getValue(String.class);
                        tambahData(getNama,getNik);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(TambahPengaduan.this,Home.class);
                startActivity(back);
                finish();
            }
        });

        if (!(pengaduans == null)){
            EdtSubject.setText(pengaduans.getSubject());
            Edtisi.setText(pengaduans.getIsi_laporan());
            Edttgl.setText(pengaduans.getTanggal());

            btnSubmit.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateData();
                }
            });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST  && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void tambahData(String getNama, String getNik) {
        subject = EdtSubject.getText().toString();
        nama = getNama;
        isi = Edtisi.getText().toString();
        nik = getNik;
        tanggal = Edttgl.getText().toString();
        status = "belum";
        if (filePath != null){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("img");
            final StorageReference gambar = storageReference.child(filePath.getLastPathSegment());
            txtImage.setText(filePath.getLastPathSegment());
            gambar.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    gambar.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Task<Uri> uriGambar = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriGambar.isComplete()) ;
                            Uri url = uriGambar.getResult();

                            String id = db.push().getKey();
                            Pengaduan pengaduan = new Pengaduan(id,subject,nik,nama,isi,tanggal,url.toString(),status);

                            db.child(id).setValue(pengaduan).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()){
                                        Toast.makeText(TambahPengaduan.this, "Berhasil Menambah", Toast.LENGTH_SHORT).show();
                                        Intent toHome = new Intent(TambahPengaduan.this,Home.class);
                                        startActivity(toHome);
                                        finish();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }else{
            String id = db.push().getKey();
            Pengaduan pengaduan = new Pengaduan(id,subject,nik,nama,isi,tanggal,"",status);

            db.child(id).setValue(pengaduan).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()){
                        Toast.makeText(TambahPengaduan.this, "Berhasil Menambah", Toast.LENGTH_SHORT).show();
                        Intent toHome = new Intent(TambahPengaduan.this,Home.class);
                        startActivity(toHome);
                        finish();
                    }
                }
            });
        }
    }

    private void updateData(){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("pengaduan").child(pengaduans.getId_pengaduan());
        final String id_p = pengaduans.getId_pengaduan();
        final String sub = EdtSubject.getText().toString();
        final String nik = pengaduans.getNik();
        final String name = pengaduans.getNama();
        final String isi = Edtisi.getText().toString();
        final String date = Edttgl.getText().toString();
        final String img = pengaduans.getImg();
        final String status = pengaduans.getStatus();

        if (filePath != null){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("img");
            final StorageReference gambar = storageReference.child(filePath.getLastPathSegment());
            txtImage.setText(filePath.getLastPathSegment());
            gambar.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    gambar.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Task<Uri> uriGambar = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriGambar.isComplete()) ;
                            Uri url = uriGambar.getResult();


                            Pengaduan pengaduan = new Pengaduan(id_p,sub,nik,name,isi,date,url.toString(),status);
                            reference.setValue(pengaduan).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(TambahPengaduan.this, "Data Diubah", Toast.LENGTH_SHORT).show();
                                    Intent toHome = new Intent(TambahPengaduan.this,Home.class);
                                    startActivity(toHome);
                                    finish();
                                }
                            });
                        }
                    });
                }
            });
        }else{
            Pengaduan pengaduan = new Pengaduan(id_p,sub,nik,name,isi,date,img,status);
            reference.setValue(pengaduan).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(TambahPengaduan.this, "Data Diubah", Toast.LENGTH_SHORT).show();
                    Intent toHome = new Intent(TambahPengaduan.this,Home.class);
                    startActivity(toHome);
                    finish();
                }
            });
        }
    }
}
