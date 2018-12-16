package com.example.mateu.appgastos.DAO;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mateu.appgastos.DAO.Gasto;
import com.example.mateu.appgastos.DAO.GastosDAO;
import com.example.mateu.appgastos.R;

import java.util.List;

public class GastosAdapter extends RecyclerView.Adapter<GastosHolder> {

    private final List<Gasto> gastos;

    public GastosAdapter(List<Gasto> gastos) {
        this.gastos = gastos;
    }

    @Override
    public GastosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GastosHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linha_gastos, parent, false));
    }

    @Override
    public void onBindViewHolder(GastosHolder holder, final int position) {
        holder.item.setText(gastos.get(position).getItem());
        holder.valor.setText(gastos.get(position).getValor());
        final long deleteDbID = gastos.get(position).getID();
        final int deleteViewID = holder.getAdapterPosition();
        if(gastos.get(position).getIdIMG()==0){
            holder.cat.setImageResource(R.drawable.casahome);
        }else if (gastos.get(position).getIdIMG()==1){
            holder.cat.setImageResource(R.drawable.beleza);
        }else if (gastos.get(position).getIdIMG()==2){
            holder.cat.setImageResource(R.drawable.comida);
        }else if (gastos.get(position).getIdIMG()==3){
            holder.cat.setImageResource(R.drawable.cartao);
        }else if (gastos.get(position).getIdIMG()==4){
            holder.cat.setImageResource(R.drawable.outros);
        }

        holder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir este item?")
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GastosDAO dao = new GastosDAO(view.getContext());
                                int numItens = dao.excluirItem(deleteDbID);
                                if (numItens > 0) {
                                    gastos.remove(deleteViewID);
                                    notifyItemRemoved(deleteViewID);
                                    notifyItemRangeChanged(deleteViewID, gastos.size());
                                    Snackbar.make(view, "Excluiu!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {
                                    Snackbar.make(view, "Erro ao excluir o item!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return gastos != null ? gastos.size() : 0;
    }

    public void adicionarCompra(Gasto gasto) {
        gastos.add(gasto);
        notifyItemInserted(getItemCount());
    }

    public void removerCompra(int deleteViewID) {
        gastos.remove(deleteViewID);
        notifyItemRemoved(deleteViewID);
        notifyItemRangeChanged(deleteViewID, gastos.size());
    }
    public void cancelarRemocao(int deleteViewID){
        notifyItemChanged(deleteViewID);
    }
    public long getDbID(int deleteViewID){
        return gastos.get(deleteViewID).getID();
    }


}