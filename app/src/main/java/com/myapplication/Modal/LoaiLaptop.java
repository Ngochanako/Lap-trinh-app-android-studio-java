package com.myapplication.Modal;

import java.io.Serializable;

public class LoaiLaptop implements Serializable {
    private String maloai;

    private String tenloai;


    @Override
    public String toString() {
        return "LoaiLaptop{" +
                "maloai='" + maloai + '\'' +
                ", tenloai='" + tenloai + '\'' +
                '}';
    }

    public String getMaloai() {
        return maloai;
    }

    public void setMaloai(String maloai) {
        this.maloai = maloai;
    }



    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public LoaiLaptop(String maloai, String tenloai) {
        this.maloai = maloai;

        this.tenloai = tenloai;
    }
    public LoaiLaptop() {

    }
}
