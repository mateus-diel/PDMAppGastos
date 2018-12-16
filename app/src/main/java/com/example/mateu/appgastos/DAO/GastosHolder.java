package com.example.mateu.appgastos.DAO;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mateu.appgastos.R;

public class GastosHolder extends RecyclerView.ViewHolder {


    public TextView item;
    public TextView valor;
    public ImageView btnExcluir;
    public ImageView cat;

    public GastosHolder(View itemView) {
        super(itemView);
        item = itemView.findViewById(R.id.tv_linha_itemID);
        valor = itemView.findViewById(R.id.tv_linha_valorID);
        btnExcluir = itemView.findViewById(R.id.iv_linha_deleteID);
        cat = itemView.findViewById(R.id.iv_Cat);
    }
}