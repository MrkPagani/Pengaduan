package com.example.pengaduan;

public class Tanggapan {
    private String id_tanggapan,id_pengaduan,tgl_pengaduan,tanggapan,nama_petugas;

    public Tanggapan() {
    }

    public Tanggapan(String id_tanggapan, String id_pengaduan, String tgl_pengaduan, String tanggapan, String nama_petugas) {
        this.id_tanggapan = id_tanggapan;
        this.id_pengaduan = id_pengaduan;
        this.tgl_pengaduan = tgl_pengaduan;
        this.tanggapan = tanggapan;
        this.nama_petugas = nama_petugas;
    }

    public String getId_tanggapan() {
        return id_tanggapan;
    }

    public void setId_tanggapan(String id_tanggapan) {
        this.id_tanggapan = id_tanggapan;
    }

    public String getId_pengaduan() {
        return id_pengaduan;
    }

    public void setId_pengaduan(String id_pengaduan) {
        this.id_pengaduan = id_pengaduan;
    }

    public String getTgl_pengaduan() {
        return tgl_pengaduan;
    }

    public void setTgl_pengaduan(String tgl_pengaduan) {
        this.tgl_pengaduan = tgl_pengaduan;
    }

    public String getTanggapan() {
        return tanggapan;
    }

    public void setTanggapan(String tanggapan) {
        this.tanggapan = tanggapan;
    }

    public String getNama_petugas() {
        return nama_petugas;
    }

    public void setNama_petugas(String nama_petugas) {
        this.nama_petugas = nama_petugas;
    }
}
