package com.example.mateu.appgastos.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class GastosDAO {
    private final String TABLE_GASTOS = "Gastos";
    private DB_Gateway gw;

    public GastosDAO(Context ctx){
        gw = DB_Gateway.getInstance(ctx);
    }


    // retorna o ID da inserção
    public long salvarItem(Gasto gasto){
        ContentValues cv = new ContentValues();
        cv.put("Item", gasto.getItem());
        cv.put("valor", gasto.getValor());
        cv.put("Icon", gasto.getIdIMG());
        return gw.getDatabase().insert(TABLE_GASTOS, null, cv);
    }

    // retorna a quantidade de linhas afetadas
    public int excluirItem(long id){
        return gw.getDatabase().delete(TABLE_GASTOS, "ID=?", new String[]{ id + "" });
    }

    public List<Gasto> retornarTodos(){
        List<Gasto> gasto = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Gastos", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String item = cursor.getString(cursor.getColumnIndex("Item"));
            String valor = cursor.getString(cursor.getColumnIndex("Valor"));
            int idimg = cursor.getInt(cursor.getColumnIndex("Icon"));
            gasto.add(new Gasto(id, item, valor,idimg));
        }
        cursor.close();
        return gasto;
    }

    public void recriarTabela(){
        gw.getDatabase().execSQL("DROP TABLE IF EXISTS Gastos");
        gw.getDatabase().execSQL("CREATE TABLE Gastos (ID INTEGER PRIMARY KEY AUTOINCREMENT, Item TEXT NOT NULL, Valor TEXT, Icon INTEGER)");
    }

}