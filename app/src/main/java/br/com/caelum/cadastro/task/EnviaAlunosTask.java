package br.com.caelum.cadastro.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.model.Aluno;
import br.com.caelum.cadastro.AlunoConverter;
import support.WebClient;

/**
 * Created by android6017 on 01/06/16.
 */
public class EnviaAlunosTask extends AsyncTask<Object, Object, String>
{
    private Context contexto;
    private ProgressDialog pd;


    public EnviaAlunosTask(Context contexto) {
        this.contexto = contexto;
    }

    @Override
    protected String doInBackground(Object... params) {

        AlunoDAO dao = new AlunoDAO(contexto);
        List<Aluno> alunos = dao.getLista();
        dao.close();

        String json = new AlunoConverter().toJSON(alunos);
        WebClient client = new WebClient();


        String respota = client.post(json);
        return respota;




    }


        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(contexto, result, Toast.LENGTH_LONG).show();
            pd.dismiss();
        }

        @Override
        protected void onPreExecute()
        {
            pd = ProgressDialog.show(contexto, "Aguarde","Enviando Dados Para o Servidor...", true, true);
        }
    }
