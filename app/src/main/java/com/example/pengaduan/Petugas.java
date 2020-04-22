package com.example.pengaduan;

public class Petugas {

    private String id_petugas,nama_petugas,username,telp,level;

    public Petugas() {
    }

    public Petugas(String id_petugas, String nama_petugas, String username, String telp, String level) {
        this.id_petugas = id_petugas;
        this.nama_petugas = nama_petugas;
        this.username = username;
        this.telp = telp;
        this.level = level;
    }

    public String getId_petugas() {
        return id_petugas;
    }

    public void setId_petugas(String id_petugas) {
        this.id_petugas = id_petugas;
    }

    public String getNama_petugas() {
        return nama_petugas;
    }

    public void setNama_petugas(String nama_petugas) {
        this.nama_petugas = nama_petugas;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
