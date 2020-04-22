package com.example.pengaduan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterTanggapan extends RecyclerView.Adapter<AdapterTanggapan.MyViewHolder> {

    List<Tanggapan> tanggapan;
    DatabaseReference db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String level,nama;

    public AdapterTanggapan(List<Tanggapan> tanggapan){
        this.tanggapan = tanggapan;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tanggapan,parent,false);
        return new AdapterTanggapan.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterTanggapan.MyViewHolder holder, int position) {
        final Tanggapan list = tanggapan.get(position);
        holder.username.setText(list.getNama_petugas());
        holder.isi.setText(list.getTanggapan());
        holder.tanggal.setText(list.getTgl_pengaduan());
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        final String userId = currentUser.getUid();
        db = FirebaseDatabase.getInstance().getReference("user").child(userId);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                level = dataSnapshot.child("level").getValue(String.class);
                nama = dataSnapshot.child("nama").getValue(String.class);
                if (level.equals("2")){
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(final View v) {
                            final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                            alert.setTitle("Option");
                            alert.setMessage("Apakah akan menghapus pengaduan ?");
                            alert.setCancelable(true);
                            alert.create();


                            alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("tanggapan").child(list.getId_tanggapan());
                                    db.removeValue();
                                    Toast.makeText(v.getContext(), "Data Dihapus", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                            alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.show();
                            return false;
                        }
                    });

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View dialogView = inflater.inflate(R.layout.dialog_tanggapan,null);
                            alert.setView(dialogView);
                            alert.setTitle("Tanggapan");
                            alert.setCancelable(true);
                            alert.create();

                            final EditText edtTanggapan = dialogView.findViewById(R.id.edtTanggapan);
                            final EditText edtTanggal = dialogView.findViewById(R.id.edtTanggal);
                            Spinner spinner = dialogView.findViewById(R.id.spinnerProses);
                            spinner.setVisibility(View.INVISIBLE);

                            edtTanggal.setText(list.getTgl_pengaduan());
                            edtTanggapan.setText(list.getTanggapan());

                            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tanggapan").child(list.getId_tanggapan());
                                    String id = list.getId_tanggapan();
                                    String id_pengaduan = list.getId_pengaduan();
                                    String tanggal = edtTanggal.getText().toString();
                                    String isiTanggapan = edtTanggapan.getText().toString();
                                    String id_petugas = nama;

                                    Tanggapan tanggapan = new Tanggapan(id,id_pengaduan,tanggal,isiTanggapan,id_petugas);

                                    reference.setValue(tanggapan);
                                    Toast.makeText(v.getContext(), "Data Diubah", Toast.LENGTH_SHORT).show();
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
                    });
                } else if (level.equals("3")){
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(final View v) {
                            final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                            alert.setTitle("Option");
                            alert.setMessage("Apakah akan menghapus pengaduan ?");
                            alert.setCancelable(true);
                            alert.create();


                            alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("tanggapan").child(list.getId_tanggapan());
                                    db.removeValue();
                                    Toast.makeText(v.getContext(), "Data Dihapus", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                            alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.show();
                            return false;
                        }
                    });

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View dialogView = inflater.inflate(R.layout.dialog_tanggapan,null);
                            alert.setView(dialogView);
                            alert.setTitle("Tanggapan");
                            alert.setCancelable(true);
                            alert.create();

                            final EditText edtTanggapan = dialogView.findViewById(R.id.edtTanggapan);
                            final EditText edtTanggal = dialogView.findViewById(R.id.edtTanggal);
                            Spinner spinner = dialogView.findViewById(R.id.spinnerProses);
                            spinner.setVisibility(View.INVISIBLE);

                            edtTanggal.setText(list.getTgl_pengaduan());
                            edtTanggapan.setText(list.getTanggapan());

                            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tanggapan").child(list.getId_tanggapan());
                                    String id = list.getId_tanggapan();
                                    String id_pengaduan = list.getId_pengaduan();
                                    String tanggal = edtTanggal.getText().toString();
                                    String isiTanggapan = edtTanggapan.getText().toString();
                                    String nama_petugas = nama;

                                    Tanggapan tanggapan = new Tanggapan(id,id_pengaduan,tanggal,isiTanggapan,nama_petugas);

                                    reference.setValue(tanggapan);
                                    Toast.makeText(v.getContext(), "Data Diubah", Toast.LENGTH_SHORT).show();
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
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return tanggapan.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username,isi,tanggal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.namaPetugas);
            isi = itemView.findViewById(R.id.isiTanggapan);
            tanggal = itemView.findViewById(R.id.tanggal);
        }
    }
}
