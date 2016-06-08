package br.com.caelum.cadastro.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.model.Prova;

/**
 * Created by android6017 on 01/06/16.
 */
public class DetalhesProvaFragment extends Fragment {

    private Prova prova;
    private TextView materia;
    private TextView data;
    private ListView topicos;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layoutDetalhes = inflater.inflate(R.layout.fragment_detalhes_provas, container, false);

        Bundle args = getArguments();
        buscaComponentes(layoutDetalhes);



        if(args !=null)
        {
            this.prova = (Prova) args.getSerializable("prova");
            populaCamposComDados(prova);
        }
        buscaComponentes(layoutDetalhes);




        return layoutDetalhes;
    }

    private void buscaComponentes(View layout)
    {
        this.materia = (TextView) layout.findViewById(R.id.detalhe_prova_materia);
        this.data    = (TextView) layout.findViewById(R.id.detalhe_prova_data);
        this.topicos = (ListView) layout.findViewById(R.id.detalhe_prova_topicos);

    }

    public void populaCamposComDados(Prova prova)
    {
        if(prova != null)
        {
            this.materia.setText(prova.getMateria());
            this.data.setText(prova.getData());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, prova.getTopicos());
            this.topicos.setAdapter(adapter);
        }
    }

}
