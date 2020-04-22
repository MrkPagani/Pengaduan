package com.example.pengaduan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DetailPengaduan extends AppCompatActivity {

    List<Tanggapan> list;
    TextView txtNama,txtIsi,txtTanggal,txtNik,txtSubject,txtStatus;
    ImageView btnBack,imgLaporan,btnUpdate;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference ref,db;
    FirebaseStorage storage;
    StorageReference storageReference;
    FloatingActionButton btnTanggapan,btnGenerate;
    RecyclerView recyclerView;
    AdapterTanggapan adapter;
    String isiTanggapan,id_pengaduan,tanggal,id_petugas;
    Pengaduan pengaduan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengaduan);
        pengaduan = getIntent().getParcelableExtra("PENGADUAN");
        txtNama = findViewById(R.id.nama);
        txtIsi = findViewById(R.id.laporan);
        txtTanggal = findViewById(R.id.tanggal);
        txtNik = findViewById(R.id.nik);
        txtSubject = findViewById(R.id.subject);
        txtStatus = findViewById(R.id.status);
        btnBack = findViewById(R.id.back);
        btnTanggapan = findViewById(R.id.btnTanggapan);
        imgLaporan = findViewById(R.id.imgLaporan);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnGenerate = findViewById(R.id.btnGenerate);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String userId = currentUser.getUid();
        db = FirebaseDatabase.getInstance().getReference("tanggapan");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        recyclerView = findViewById(R.id.recyclerView);

        txtNama.setText(pengaduan.getNama());
        txtIsi.setText(pengaduan.getIsi_laporan());
        txtTanggal.setText(pengaduan.getTanggal());
        txtNik.setText(pengaduan.getNik());
        txtSubject.setText(pengaduan.getSubject());
        txtStatus.setText(pengaduan.getStatus());
        Glide.with(this).load(pengaduan.getImg()).into(imgLaporan);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(DetailPengaduan.this,Home.class);
                startActivity(back);
                finish();
            }
        });

        btnTanggapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference("user").child(userId);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nama = dataSnapshot.child("nama").getValue(String.class);
                        showDialogTanggapan(nama);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toForm = new Intent(DetailPengaduan.this,TambahPengaduan.class);
                toForm.putExtra("PENGADUAN",pengaduan);
                startActivity(toForm);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("user").child(userId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String level = dataSnapshot.child("level").getValue(String.class);
                if (level != null) {
                    updateLayout(level);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateLayout(String level) {
        switch (level) {
            case "1":
                btnTanggapan.hide();
                btnGenerate.hide();
                break;
            case "2":
                btnUpdate.setVisibility(View.GONE);
                btnGenerate.hide();
                break;
            case "3":
                btnGenerate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            generateLaporan();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }

    private void showDialogTanggapan(final String nama) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_tanggapan,null);
        alert.setView(dialogView);
        alert.setTitle("Tanggapan");
        alert.setCancelable(true);

        final Spinner spinner = dialogView.findViewById(R.id.spinnerProses);
        final EditText edtTanggapan = dialogView.findViewById(R.id.edtTanggapan);
        final EditText edtTanggal = dialogView.findViewById(R.id.edtTanggal);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("pengaduan").child(pengaduan.getId_pengaduan());
                String id_p = pengaduan.getId_pengaduan();
                String subject = pengaduan.getSubject();
                String name = pengaduan.getNama();
                String date = pengaduan.getTanggal();
                String nik = pengaduan.getNik();
                String isi = pengaduan.getIsi_laporan();
                String img = pengaduan.getImg();
                String status = spinner.getSelectedItem().toString();

                Pengaduan pengaduan = new Pengaduan(id_p,subject,nik,name,isi,date,img,status);

                reference.setValue(pengaduan);

                id_pengaduan = pengaduan.getId_pengaduan();
                tanggal = edtTanggal.getText().toString();
                isiTanggapan = edtTanggapan.getText().toString();
                id_petugas = nama;

                String id = db.push().getKey();
                Tanggapan tanggapan = new Tanggapan(id,id_pengaduan,tanggal,isiTanggapan,id_petugas);
                db.child(id).setValue(tanggapan).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DetailPengaduan.this, "Berhasil Menambah Tanggapan", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void generateLaporan() throws FileNotFoundException, DocumentException {
        File myFile = new File( "/sdcard/"+pengaduan.getSubject() + ".pdf");
        Toast.makeText(this, "Pdf Tersimpan " + myFile, Toast.LENGTH_SHORT).show();

        Document document = new Document();
        PdfWriter.getInstance(document,new FileOutputStream(myFile.getAbsoluteFile()));
        document.open();

        PdfPTable table = new PdfPTable(3);
        PdfPCell cell = new PdfPCell(new Phrase("Tanggal"));
        PdfPCell cell2 = new PdfPCell(new Phrase("Nama"));
        PdfPCell cell3 = new PdfPCell(new Phrase("Isi Laporan"));
        table.addCell(cell);
        table.addCell(cell2);
        table.addCell(cell3);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.completeRow();

        PdfPTable table2 = new PdfPTable(3);
        PdfPCell cell5 = new PdfPCell(new Phrase(pengaduan.getNama()));
        PdfPCell cell4 = new PdfPCell(new Phrase(pengaduan.getTanggal()));
        PdfPCell cell6 = new PdfPCell(new Phrase(pengaduan.getIsi_laporan()));
        table2.addCell(cell4);
        table2.addCell(cell5);
        table2.addCell(cell6);
        table2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.completeRow();

        Paragraph subject = new Paragraph(pengaduan.getSubject());
        subject.setAlignment(Element.ALIGN_CENTER);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN,21);
        subject.setFont(font);
        Paragraph space = new Paragraph("");

        document.add(subject);
        document.add(space);
        document.add(table);
        document.add(table2);

        document.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        list = new ArrayList<>();
        db.orderByChild("id_pengaduan").equalTo(pengaduan.getId_pengaduan()).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snap:dataSnapshot.getChildren()){
                    Tanggapan tanggapan = snap.getValue(Tanggapan.class);
                    list.add(tanggapan);
                }
                adapter = new AdapterTanggapan(list);
                recyclerView.setLayoutManager(new LinearLayoutManager(DetailPengaduan.this));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
