package com.example.pengaduan;

public class Masyarakat {
    private String id,nik,nama,username,password,telp,level;

    public Masyarakat() {
    }

    public Masyarakat(String id,String nik, String nama, String username, String password, String telp,String level) {
        this.id = id;
        this.nik = nik;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.telp = telp;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
