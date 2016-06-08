package br.com.caelum.cadastro.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.caelum.cadastro.FormularioActivity;
import br.com.caelum.cadastro.R;

/**
 * Created by android6017 on 25/05/16.
 */
public class FormularioHelper {

    private Aluno aluno;
    private EditText nome;
    private EditText telefone;
    private EditText site;
    private RatingBar nota;
    private EditText endereco;
    private Long id;
    private ImageView foto;
    private Button fotoButton;

    public FormularioHelper(FormularioActivity activity)
    {
        this.aluno = new Aluno();

        this.nome = (EditText) activity.findViewById(R.id.nome);
        this.telefone = (EditText) activity.findViewById(R.id.telefone);
        this.site = (EditText) activity.findViewById(R.id.site);
        this.endereco = (EditText) activity.findViewById(R.id.endereco);
        this.nota = (RatingBar) activity.findViewById(R.id.nota);

        this.fotoButton = (Button) activity.findViewById(R.id.formulario_foto_button);
        this.foto = (ImageView) activity.findViewById(R.id.formulario_foto);


    }

    public Button getFotoButton()
    {
        return fotoButton;
    }

    public Aluno pegaAlunoDoFormulario()
    {
        aluno.setNome(nome.getText().toString());
        aluno.setEndereco(endereco.getText().toString());
        aluno.setSite(site.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        aluno.setNota(Double.valueOf(nota.getProgress()));
        aluno.setCaminhoFoto((String) foto.getTag());


        return aluno;

    }
    public boolean temNome()
    {
        return !nome.getText().toString().isEmpty();
    }

    public void mostraErro()
    {
        nome.setError("Campo nome nao pode ser branco");
    }

    public void colocaDadosNoFormulario(Aluno aluno)
    {
        nome.setText(aluno.getNome());
        endereco.setText(aluno.getEndereco());
        site.setText(aluno.getSite());
        telefone.setText(aluno.getTelefone());
        nota.setProgress(aluno.getNota().intValue());
        id = aluno.getId();

        carregarImagem(aluno.getCaminhoFoto());


        this.aluno = aluno;

    }

    public void carregarImagem(String caminhoFoto) {
        if (caminhoFoto != null) {
            Bitmap bmFoto = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bmFotoRecebida = Bitmap.createScaledBitmap(bmFoto, 400, 300, true);
            foto.setImageBitmap(bmFotoRecebida);
            foto.setScaleType(ImageView.ScaleType.FIT_XY);
            foto.setTag(caminhoFoto);
        }
    }

}
