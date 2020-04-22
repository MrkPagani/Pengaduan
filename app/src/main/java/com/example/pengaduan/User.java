package com.example.pengaduan;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String id_user,nama,email,nik,telp,level;

    public User() {
    }

    public User(String id_user, String nama, String email, String nik, String telp, String level) {
        this.id_user = id_user;
        this.nama = nama;
        this.email = email;
        this.nik = nik;
        this.telp = telp;
        this.level = level;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_user);
        dest.writeString(this.nama);
        dest.writeString(this.email);
        dest.writeString(this.nik);
        dest.writeString(this.telp);
        dest.writeString(this.level);
    }

    protected User(Parcel in){
        this.id_user = in.readString();
        this.nama = in.readString();
        this.email = in.readString();
        this.nik = in.readString();
        this.telp = in.readString();
        this.level = in.readString();
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
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
