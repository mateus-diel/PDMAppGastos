package com.example.mateu.appgastos;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mateu.appgastos.DAO.Gasto;
import com.example.mateu.appgastos.DAO.GastosAdapter;
import com.example.mateu.appgastos.DAO.GastosDAO;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private GastosAdapter adapter;

    private TextView seu_nome;
    private TextView seu_email;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ConstraintLayout main_layout;


    private static final int activity_dados_pessoais = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_layout = findViewById(R.id.main_layoutID);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.include_main).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_cadastro).setVisibility(View.VISIBLE);
            }
        });

        FloatingActionButton fabD = (FloatingActionButton) findViewById(R.id.fabD);
        fabD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.include_main).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_despesas).setVisibility(View.INVISIBLE);
                findViewById(R.id.include_cadastro).setVisibility(View.VISIBLE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // Obtem a referência do layout de navegação
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtem a referência da view de cabeçalho (importante para achar os componentes)
        View headerView = navigationView.getHeaderView(0);
        seu_nome = headerView.findViewById(R.id.seuNomeID);
        seu_email = headerView.findViewById(R.id.seuEmailID);

        // Inicialização para gravar as preferencias
        pref = getSharedPreferences("ListaComprasPrefArq", MODE_PRIVATE);
        editor = pref.edit();

        // Verifica se já foi gravado valores
        if (pref.contains("Nome")) {
            seu_nome.setText(pref.getString("Nome", "sem nome"));
            seu_email.setText(pref.getString("Email", "sem email"));
        } else {
            Snackbar.make(main_layout, "Por favor configure seu nome e email", Snackbar.LENGTH_LONG).show();
            seu_nome.setText("sem nome");
            seu_email.setText("sem email");
        }

        //        Botões de Incluir/Cancelar/Limpar
        Button btnCancelar = (Button) findViewById(R.id.btn_cancelarID);
        btnCancelar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.include_main).setVisibility(View.VISIBLE);
                findViewById(R.id.include_cadastro).setVisibility(View.INVISIBLE);
            }
        });


        Button btnLimpar = (Button) findViewById(R.id.btn_limparID);
        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button btnSalvar = (Button) findViewById(R.id.btn_salvarID);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtItem = findViewById(R.id.et_itemID);
                EditText txtValor = findViewById(R.id.et_valorID);

                //pegando os valores
                String item = txtItem.getText().toString();
                String valor = txtValor.getText().toString();
                if (item.equals("")) {
                    Snackbar.make(view, "Preencha o item!", Snackbar.LENGTH_SHORT).show();
                } else {
                    //salvando os dados
                    Gasto gasto = new Gasto(0, item, valor,getIdCategoria());
                    GastosDAO dao = new GastosDAO(getBaseContext());
                    long salvoID = dao.salvarItem(gasto);
                    if (salvoID != -1) {
                        //limpa os campos
                        txtValor.setText("");
                        txtItem.setText("");

                        //adiciona no recyclerView
                        gasto.setID(salvoID);
                        adapter.adicionarCompra(gasto);

                        Snackbar.make(view, "Salvo!", Snackbar.LENGTH_LONG).show();
                        findViewById(R.id.include_main).setVisibility(View.VISIBLE);
                        findViewById(R.id.include_cadastro).setVisibility(View.INVISIBLE);
                    } else {
                        Snackbar.make(view, "Erro ao salvar Item, consulte os logs!", Snackbar.LENGTH_LONG).show();
                        findViewById(R.id.include_main).setVisibility(View.VISIBLE);
                        findViewById(R.id.include_cadastro).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                int count = radioGroup.getChildCount();
                for (int i=0;i<count;i++) {
                    View o = radioGroup.getChildAt(i);
                    if (o instanceof RadioButton) {
                        ((RadioButton) o).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
                rb.setTextColor(getResources().getColor(R.color.Blue));
                Toast.makeText(getBaseContext(), rb.getText(), Toast.LENGTH_SHORT).show();

            }
        });


        configurarRecycler();
    }

    private int getIdCategoria(){
        final int[] value = {5};
        value[0]=4;
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        if (radioGroup.getCheckedRadioButtonId()!=-1) {
            RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            if (String.valueOf(rb.getText()).equalsIgnoreCase("Casa")) {
                value[0] = 0;
            } else if (String.valueOf(rb.getText()).equalsIgnoreCase("Salão")) {
                value[0] = 1;
            }
            if (String.valueOf(rb.getText()).equalsIgnoreCase("Comida")) {
                value[0] = 2;
            }
            if (String.valueOf(rb.getText()).equalsIgnoreCase("Cartão")) {
                value[0] = 3;
            }
            if (String.valueOf(rb.getText()).equalsIgnoreCase("Outros")) {
                value[0] = 4;
            }
        }
        return value[0];
    }

    private void configurarRecycler() {
        // Configurando o gerenciador de layout para ser uma lista.
        recyclerView = (RecyclerView) findViewById(R.id.despesas_recyclerViewID);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        GastosDAO dao = new GastosDAO(this);
        adapter = new GastosAdapter(dao.retornarTodos());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Adicionar o arrastar para direita para excluir item
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(addArrastarItem());
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    // Recebendo retorno de activity chamadas
    protected void onActivityResult(int codigo, int resultado, Intent i) {

        // se o resultado de uma Activity for da Activity_DADOS_PESSOIS
        if (codigo == activity_dados_pessoais) {
            // se o "i" (Intent) estiver preenchido, pega os seus dados (getExtras())
            Bundle params = i != null ? i.getExtras() : null;
            if (params != null) {
                seu_nome.setText(params.getString("Nome"));
                seu_email.setText(params.getString("Email"));
            }
        }
    }

    public ItemTouchHelper.SimpleCallback addArrastarItem() {
        ItemTouchHelper.SimpleCallback deslizarItem = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(getBaseContext(), "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int deleteViewID = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir este item? ")
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GastosDAO dao = new GastosDAO(getBaseContext());
                                int numItens = dao.excluirItem(adapter.getDbID(deleteViewID));
                                if (numItens > 0) {
                                    adapter.removerCompra(deleteViewID);
                                    Snackbar.make(main_layout, "Excluido com sucesso!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {
                                    Snackbar.make(main_layout, "Erro ao excluir o item!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(main_layout, "Cancelando...", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                adapter.cancelarRemocao(deleteViewID);
                            }
                        })
                        .create()
                        .show();
            }
        };
        return deslizarItem;
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            findViewById(R.id.include_main).setVisibility(View.VISIBLE);
            findViewById(R.id.include_despesas).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_relatorio).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_cadastro).setVisibility(View.INVISIBLE);
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, dadosPessoais.class);
            startActivityForResult(intent, activity_dados_pessoais);
            return true;
        }
      /*  if (id == R.id.menu_shareID) {
            compartilhar();
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            findViewById(R.id.include_main).setVisibility(View.VISIBLE);
            findViewById(R.id.include_despesas).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_relatorio).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_cadastro).setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_relatorio) {
            findViewById(R.id.include_despesas).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_main).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_relatorio).setVisibility(View.VISIBLE);
            findViewById(R.id.include_cadastro).setVisibility(View.INVISIBLE);
            PieChart chart = (PieChart) findViewById(R.id.chart);
            float[] um = {0};
            float[] d = {0};
            float[] t = {0};
            float[] q = {0};
            float[] c = {0};
            GastosDAO g = new GastosDAO(getBaseContext());
            for (Gasto ga:g.retornarTodos()){
                if (ga.getIdIMG()==0){
                    um[0]=um[0]+Float.parseFloat(ga.getValor());
                } else if(ga.getIdIMG()==1){
                    d[0]=d[0]+Float.parseFloat(ga.getValor());
                }else if(ga.getIdIMG()==2){
                    t[0]=t[0]+Float.parseFloat(ga.getValor());
                }else if(ga.getIdIMG()==3){
                    q[0]=q[0]+Float.parseFloat(ga.getValor());
                }else if(ga.getIdIMG()==4){
                    c[0]=c[0]+Float.parseFloat(ga.getValor());
                }
            }


            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(um[0]));
            PieDataSet dataSet = new PieDataSet(entries, "Casa"); // add entries to dataset
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData pieData = new PieData(dataSet);

           /* entries = new ArrayList<>();
            entries.add(new PieEntry(d[0]));
            dataSet = new PieDataSet(entries, "Salão"); // add entries to dataset
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);*/
            //pieData.addDataSet(dataSet);
            chart.setData(pieData);
            chart.invalidate(); // refresh
        } else if (id == R.id.nav_despesas) {
            findViewById(R.id.include_despesas).setVisibility(View.VISIBLE);
            findViewById(R.id.include_main).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_relatorio).setVisibility(View.INVISIBLE);
            findViewById(R.id.include_cadastro).setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            GastosDAO dao = new GastosDAO(this);
            List<Gasto> compras = dao.retornarTodos();
            String texto = "";
            for (Gasto gasto : compras) {
                texto = texto + gasto.getItem() +": R$"+gasto.getValor() + "\n";
            }
            sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_ajuda) {
            new AlertDialog.Builder(this)
                    .setTitle("Sobre")
                    .setMessage("Aplicativo desenvolvido como método avaliativo " +
                            "da disciplina de \"Programação para Dispositivos Móveis\"" +
                            " do Curso de Ciência da Computação da UNIJUÍ!")
                    .setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                    .show();
        } else if (id == R.id.action_settings){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
