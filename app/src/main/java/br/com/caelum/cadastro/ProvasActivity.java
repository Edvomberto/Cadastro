package br.com.caelum.cadastro;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import br.com.caelum.cadastro.fragment.DetalhesProvaFragment;
import br.com.caelum.cadastro.fragment.ListaProvasFragment;
import br.com.caelum.cadastro.model.Prova;

/**
 * Created by android6017 on 01/06/16.
 */
public class ProvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(isTablet())
        {
            transaction.replace(R.id.prova_lista, new ListaProvasFragment());
            transaction.replace(R.id.provas_detalhe, new DetalhesProvaFragment());
        }
        else {
            transaction.replace(R.id.provas_view, new ListaProvasFragment());
        }


        transaction.commit();
    }

    public void selecionaProva(Prova prova)
    {
        FragmentManager fm = getSupportFragmentManager();

        if(isTablet())
        {
            DetalhesProvaFragment fragment = (DetalhesProvaFragment) fm.findFragmentById(R.id.provas_detalhe);
            fragment.populaCamposComDados(prova);

        }
        else
        {
            Bundle arqs = new Bundle();
            arqs.putSerializable("prova", prova);

            DetalhesProvaFragment fragment = new DetalhesProvaFragment();

            fragment.setArguments(arqs);

            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.provas_view, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }




    }


    private boolean isTablet()
    {
        return getResources().getBoolean(R.bool.isTablet);
    }

}
