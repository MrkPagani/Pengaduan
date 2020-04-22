package com.example.pengaduan;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterPengaduan extends RecyclerView.Adapter<AdapterPengaduan.MyViewHolder> {

    private List<Pengaduan> pengaduan;

    AdapterPengaduan(List<Pengaduan> pengaduan){
        this.pengaduan = pengaduan;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengaduan,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Pengaduan list = pengaduan.get(position);
        holder.username.setText(list.getNama());
        holder.subject.setText(list.getSubject());
        holder.status.setText(list.getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),DetailPengaduan.class);
                i.putExtra("PENGADUAN",list);
                v.getContext().startActivity(i);
            }
        });

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
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("pengaduan").child(list.getId_pengaduan());
                        Toast.makeText(v.getContext(), "Data Dihapus", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        db.removeValue();
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


    }


    @Override
    public int getItemCount() {
        return pengaduan.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username,subject,status;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.name);
            subject = itemView.findViewById(R.id.laporan);
            status = itemView.findViewById(R.id.status);
        }
    }
}
