package com.example.pengaduan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Pengaduan> list;
    AdapterPengaduan adapter;
    DatabaseReference db,ref,reference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    RecyclerView recycle;
    ImageView btn;
    TextView logout;
    NavigationView Navigation;
    ProgressBar pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recycle = findViewById(R.id.recycle);
        db = FirebaseDatabase.getInstance().getReference("pengaduan");
        btn = findViewById(R.id.btn);
        logout = findViewById(R.id.title);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String userId = currentUser.getUid();
        pg = findViewById(R.id.progressBar);
        pg.setVisibility(RecyclerView.VISIBLE);
        Navigation = findViewById(R.id.navView);
        ref = FirebaseDatabase.getInstance().getReference("user").child(userId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nama = dataSnapshot.child("nama").getValue(String.class);
                String nik = dataSnapshot.child("nik").getValue(String.class);
                String level = dataSnapshot.child("level").getValue(String.class);
                updateDrawer(nama,nik,level);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateDrawer(String nama, String nik,String level){
        Navigation.setNavigationItemSelectedListener(this);
        View header = Navigation.getHeaderView(0);
        TextView headerName = header.findViewById(R.id.tvNama);
        TextView headerNik = header.findViewById(R.id.tvNik);
        Menu menu = Navigation.getMenu();
        if(level.equals("2")){
            MenuItem item = menu.findItem(R.id.add);
            headerNik.setVisibility(View.INVISIBLE);
            item.setVisible(false);
            headerName.setText(nama);
        }else if(level.equals("3")){
            headerName.setText(nama);
            headerNik.setVisibility(View.INVISIBLE);
        }else {
            headerName.setText(nama);
            headerNik.setText(nik);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Intent moveToLogin = new Intent(Home.this, Login.class);
                startActivity(moveToLogin);
                finish();
                FirebaseAuth.getInstance().signOut();
                return true;
            case R.id.add:
                Intent toAdd = new Intent(Home.this,TambahPengaduan.class);
                startActivity(toAdd);
                finish();
                return true;
            case R.id.about:
                Intent toAbout = new Intent(Home.this,About.class);
                startActivity(toAbout);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        list = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pg.setVisibility(View.INVISIBLE);
                list.clear();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    Pengaduan pengaduan = snap.getValue(Pengaduan.class);
                    list.add(pengaduan);
                }
                adapter = new AdapterPengaduan(list);

                recycle.setLayoutManager(new LinearLayoutManager(Home.this));
                recycle.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
