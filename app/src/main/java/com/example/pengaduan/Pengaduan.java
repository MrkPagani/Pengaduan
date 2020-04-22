package com.example.pengaduan;

import android.os.Parcel;
import android.os.Parcelable;

public class Pengaduan implements Parcelable {

    private String id_pengaduan,subject,nik,nama,isi_laporan,tanggal,img,status;

    public Pengaduan() {
    }

    public Pengaduan(String id_pengaduan,String subject, String nik, String nama, String isi_laporan, String tanggal, String img,String status) {
        this.id_pengaduan = id_pengaduan;
        this.subject = subject;
        this.nik = nik;
        this.nama = nama;
        this.isi_laporan = isi_laporan;
        this.tanggal = tanggal;
        this.img = img;
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_pengaduan);
        dest.writeString(this.subject);
        dest.writeString(this.nik);
        dest.writeString(this.nama);
        dest.writeString(this.isi_laporan);
        dest.writeString(this.tanggal);
        dest.writeString(this.img);
        dest.writeString(this.status);
    }

    protected Pengaduan(Parcel in){
        this.id_pengaduan = in.readString();
        this.subject = in.readString();
        this.nik = in.readString();
        this.nama = in.readString();
        this.isi_laporan = in.readString();
        this.tanggal = in.readString();
        this.img = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<Pengaduan> CREATOR = new Parcelable.Creator<Pengaduan>(){

        @Override
        public Pengaduan createFromParcel(Parcel source) {
            return new Pengaduan(source);
        }

        @Override
        public Pengaduan[] newArray(int size) {
            return new Pengaduan[size];
        }
    };

    public String getId_pengaduan() {
        return id_pengaduan;
    }

    public void setId_pengaduan(String id_pengaduan) {
        this.id_pengaduan = id_pengaduan;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getIsi_laporan() {
        return isi_laporan;
    }

    public void setIsi_laporan(String isi_laporan) {
        this.isi_laporan = isi_laporan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
