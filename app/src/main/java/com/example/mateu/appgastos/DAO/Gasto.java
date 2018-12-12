package com.example.mateu.appgastos.DAO;

public class Gasto {

    private long ID;
    private String item;
    private String valor;

    public Gasto(long ID, String item, String valor) {
        this.ID = ID;
        this.item = item;
        this.valor = valor;
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
