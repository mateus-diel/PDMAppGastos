package com.example.mateu.appgastos.DAO;

public class Gasto {

    private long ID;
    private String item;
    private String valor;
    private int idIMG;

    public Gasto(long ID, String item, String valor, int img) {
        this.ID = ID;
        this.item = item;
        this.valor = valor;
        this.idIMG=img;
    }

    public int getIdIMG() {
        return idIMG;
    }

    public void setIdIMG(int idIMG) {
        this.idIMG = idIMG;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
