package com.myapplication.Modal;

import java.io.Serializable;

public class Laptop implements Serializable {
    private String ma;
    private String ten;
    private byte[] anh;
    private String loai ;

    private float kg;
    private int gia;

    @Override
    public String toString() {
        return "Laptop{" +
                "ma='" + ma + '\'' +
                ", ten='" + ten + '\'' +
                ", anh=" + anh.toString() +
                ", loai='" + loai + '\'' +
                ", kg=" + kg +
                ", gia=" + gia +
                '}';
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public float getKg() {
        return kg;
    }

    public void setKg(float kg) {
        this.kg = kg;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public Laptop(String ma, String ten, byte[] anh, String loai, float kg, int gia) {
        this.ma = ma;
        this.ten = ten;
        this.anh = anh;
        this.loai = loai;
        this.kg = kg;
        this.gia = gia;
    }
    public Laptop() {

    }
}
