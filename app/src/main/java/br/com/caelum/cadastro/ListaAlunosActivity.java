package br.com.caelum.cadastro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.cadastro.adpater.ListaAlunosAdapter;
import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.model.Aluno;
import br.com.caelum.cadastro.task.EnviaAlunosTask;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;
    private List<Aluno> alunos;
    private Aluno alunoSelecionado;

    public  static final String ALUNO_SELECIONADO = "alunoSelecionado";
    String permissaoLigar = Manifest.permission.CALL_PHONE;
    String permissaoSms   = Manifest.permission.RECEIVE_SMS;
    String permissaoLocalizacao   = Manifest.permission.ACCESS_FINE_LOCATION;

    int requestLigacao = 1;
    int requestsms = 2;
    int requestlocalizacao = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);



        Button botaoAdicionar = (Button) findViewById(R.id.botaoAdicionar);

        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });


        if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this,permissaoSms) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{permissaoSms},requestsms);
        }






        //String[] alunos = {"Anderson", "Felipe", "Guilherme"};

       /* AlunoDAO dao = new AlunoDAO(this);

        alunos = dao.getLista();   cursor.close();
        dao.close();*/


        this.listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        registerForContextMenu(listaAlunos);

        /*ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_expandable_list_item_1, alunos);


        listaAlunos.setAdapter(adapter);*/



       listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent editar = new Intent(ListaAlunosActivity.this, FormularioActivity.class);

                Aluno alunoSelecionado = (Aluno) listaAlunos.getItemAtPosition(position);

                editar.putExtra("aluno", alunoSelecionado);

                startActivity(editar);

                Toast.makeText(ListaAlunosActivity.this, "O aluno selecirequestLigacaoonado foi " + position, Toast.LENGTH_LONG).show();
            }
        });

        /*@string/google_maps_key
        listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String aluno = parent.getAdapter().getItem(position).toString();
                Toast.makeText(ListaAlunosActivity.this, "Aluno clicado " + aluno, Toast.LENGTH_LONG).show();
                return true;
            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);
        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_enviar_notas: {
                new EnviaAlunosTask(this).execute();
                return true;


            }

            case R.id.menu_receber_provas: {
                Intent provas = new Intent(this, ProvasActivity.class);
                startActivity(provas);
                return true;


            }

            case R.id.menu_mapa:
            {
                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this,permissaoLocalizacao) == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{permissaoLocalizacao},requestlocalizacao);
                }
                else {
                    Intent mapa = new Intent(this, mostraAlunosActivity.class);
                    startActivity(mapa);

                }


                return true;
            }


        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        alunoSelecionado = (Aluno) listaAlunos.getAdapter().getItem(info.position);



        MenuItem deletar = menu.add("Deletar");

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);

                dao.deletar(alunoSelecionado);
                dao.close();
                carregaLista();


                return false;
            }
        });

        MenuItem ligar = menu.add("ligar");

        ligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this,permissaoLigar) == PackageManager.PERMISSION_GRANTED)
                {
                    fazerLigacao();
                }
                else
                {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{permissaoLigar},requestLigacao);
                }
                return false;
            }
        });


        MenuItem sms = menu.add("Enviar SMS");
        Intent enviarSms = new Intent(Intent.ACTION_VIEW);

        enviarSms.setData(Uri.parse("sms:" + alunoSelecionado.getTelefone()));
        enviarSms.putExtra("sms-body", "Olá Mundo.");
        sms.setIntent(enviarSms);

        MenuItem site = menu.add("Abrir Site");
        Intent abrirSite = new Intent(Intent.ACTION_VIEW);
        abrirSite.setData(Uri.parse("http://" + alunoSelecionado.getSite()));
        site.setIntent(abrirSite);

        MenuItem mapa = menu.add("Abrir Mapa");
        Intent abrirMapa = new Intent(Intent.ACTION_VIEW);
        abrirMapa.setData(Uri.parse("geo:0,0?z=14&q=" + alunoSelecionado.getEndereco()));
        mapa.setIntent(abrirMapa);

    }

    protected void onResume()
    {

        super.onResume();
        this.carregaLista();
    }

    private void carregaLista()
    {

        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.getLista();
        dao.close();

        ListaAlunosAdapter adapter = new ListaAlunosAdapter(this, alunos);

        //ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_expandable_list_item_1, alunos);
        this.listaAlunos.setAdapter(adapter);
    }

    private void fazerLigacao()
    {
        Intent fazerLigacao = new Intent(Intent.ACTION_CALL);
        fazerLigacao.setData(Uri.parse("tel:" + alunoSelecionado.getTelefone()));
        startActivity(fazerLigacao);

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissoes, int[] resultados) {
        super.onRequestPermissionsResult(requestCode, permissoes, resultados);
        if(requestCode == requestLigacao)
        {
            if(resultados[0] == PackageManager.PERMISSION_GRANTED)
            {
                fazerLigacao();
            }
            else
            {
                Toast.makeText(this, "Nao ha permissao", Toast.LENGTH_LONG).show();

            }
        }

        if(requestCode == requestsms)
        {
            if(resultados[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permissão Concedida", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Nao ha permissao", Toast.LENGTH_LONG).show();

            }
        }

        if(requestCode == requestlocalizacao)
        {
            if(resultados[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent mapa = new Intent(this, mostraAlunosActivity.class);
                startActivity(mapa);
            }
        }
    }

}

