package com.app.gotosumbar.Model;

public class Komen {
    private String Komen, Nama;

    public Komen() {

    }

    public Komen(String komen, String nama) {
        Komen = komen;
        Nama = nama;
    }

    public String getKomen() {
        return Komen;
    }

    public void setKomen(String komen) {
        Komen = komen;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }
}
