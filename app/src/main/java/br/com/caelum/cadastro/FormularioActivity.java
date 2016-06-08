package br.com.caelum.cadastro;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.model.Aluno;
import br.com.caelum.cadastro.model.FormularioHelper;

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper helper;
    private String localArquivoFoto;
    private static final int REQUES_TIRAR_FOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        this.helper = new FormularioHelper(this);

        Intent intent = getIntent();

        Aluno alunoSelecionado = (Aluno) intent.getSerializableExtra("aluno");

        if(alunoSelecionado != null)
        {
            helper.colocaDadosNoFormulario(alunoSelecionado);
        }



        /*Button botao = (Button) findViewById(R.id.botaoSalvar);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FormularioActivity.this, "Voce clicou no botao!", Toast.LENGTH_LONG).show();
                finish();
            }
        });*/

        Button foto = helper.getFotoButton();
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localArquivoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";

                Intent tirarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(localArquivoFoto)));

                startActivityForResult(tirarFoto, REQUES_TIRAR_FOTO);
            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUES_TIRAR_FOTO)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                helper.carregarImagem(localArquivoFoto);
            }
            else {

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_formulario_ok:



                Aluno aluno = helper.pegaAlunoDoFormulario();
                //Toast.makeText(this, "Aluno adicionado" + aluno.getNome(), Toast.LENGTH_LONG).show();

                if (helper.temNome()) {

                    AlunoDAO dao = new AlunoDAO(this);

                    if(aluno.getId() != null)
                    {
                        dao.altera(aluno);
                    }
                    else {
                        dao.insere(aluno);

                    }
                    dao.close();

                    finish();
                    return false;
                }
                else helper.mostraErro();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
