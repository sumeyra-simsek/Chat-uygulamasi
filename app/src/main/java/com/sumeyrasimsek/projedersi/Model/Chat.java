package com.sumeyrasimsek.projedersi.Model;

public class Chat {
    String alici;
    String gonderen;
    String mesaj;
    String resim;
    String saat;
    String tarih;

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public Chat(String alici, String gonderen, String mesaj, String resim, String saat, String tarih) {
        this.alici = alici;
        this.gonderen = gonderen;
        this.mesaj = mesaj;
        this.resim = resim;
        this.saat = saat;
        this.tarih = tarih;
    }

    public String getAlici() {
        return alici;
    }

    public void setAlici(String alici) {
        this.alici = alici;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }



    public Chat() {
    }
}
