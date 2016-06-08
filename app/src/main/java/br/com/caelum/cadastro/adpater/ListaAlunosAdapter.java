package br.com.caelum.cadastro.adpater;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.model.Aluno;

/**
 * Created by android6017 on 31/05/16.
 */
public class ListaAlunosAdapter extends BaseAdapter {
    private final List<Aluno> alunos;
    private final Activity activity;


    public ListaAlunosAdapter(Activity activity, List<Aluno> alunos) {
        this.alunos = alunos;
        this.activity = activity;
    }

    public Object getItem(int posicao)
    {
        return alunos.get(posicao);
    }

    public int getCount()
    {
        return alunos.size();
    }

    public long getItemId(int posicao)
    {
        return alunos.get(posicao).getId();
    }

    public View getView(int posicao, View convertView, ViewGroup parent)
    {
        Aluno aluno = alunos.get(posicao);

        View view = activity.getLayoutInflater().inflate(R.layout.item, parent, false);

        TextView nome = (TextView) view.findViewById(R.id.item_nome);
        nome.setText(aluno.getNome());

        Bitmap bm;

        if(aluno.getCaminhoFoto() != null)
        {
            bm = BitmapFactory.decodeFile(aluno.getCaminhoFoto());
        }
        else
        {
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_no_image);
        }
        bm = Bitmap.createScaledBitmap(bm, 100, 100, true);

        ImageView foto =  (ImageView) view.findViewById(R.id.item_foto);
        foto.setImageBitmap(bm);

        return view;
    }
}
