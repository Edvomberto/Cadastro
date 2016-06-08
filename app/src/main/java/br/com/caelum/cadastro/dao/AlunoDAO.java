package br.com.caelum.cadastro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.cadastro.model.Aluno;

/**
 * Created by android6017 on 26/05/16.
 */
public class AlunoDAO extends SQLiteOpenHelper {

    private static final int versao = 2;
    private static final String tabela = "Alunos";
    private static final String banco = "CadastroAluno";

    public AlunoDAO(Context contexto)
    {
        super(contexto, banco,null,1 );

    }

    public void onCreate(SQLiteDatabase db)
    {
        String sql =   "CREATE TABLE " + tabela
                     + "(id INTEGER PRIMARY KEY,"
                     + "nome TEXT NOT NULL, "
                     + "telefone TEXT, "
                     + "endereco TEXT, "
                     + "site TEXT,"
                     + "nota REAL, "
                     + "caminhoFoto TEXT);";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova)
    {

        String sql = "ALTER TABLE " +  tabela + " ADD COLUMN caminhoFoto TEXT;";
        db.execSQL(sql);
        //onCreate(db);
    }

    public void insere(Aluno aluno)
    {
        ContentValues valores =  new ContentValues();

        valores.put("nome", aluno.getNome());
        valores.put("telefone", aluno.getTelefone());
        valores.put("endereco", aluno.getEndereco());
        valores.put("site", aluno.getSite());
        valores.put("nota", aluno.getNota());
        valores.put("caminhoFoto", aluno.getCaminhoFoto());

        getWritableDatabase().insert(tabela, null, valores);


    }

    public List<Aluno> getLista()
    {
        List<Aluno> alunos = new ArrayList<Aluno>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from " + tabela + ";", null);

        while (cursor.moveToNext())
        {
            Aluno aluno = new Aluno();
            aluno.setId(cursor.getLong(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);




        }
        cursor.close();
        return alunos;
    }

    public void deletar(Aluno aluno)
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(tabela, "id=?",new String[]{aluno.getId().toString()});

    }

    public void altera(Aluno aluno)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores =  new ContentValues();

        valores.put("id", aluno.getId());
        valores.put("nome", aluno.getNome());
        valores.put("telefone", aluno.getTelefone());
        valores.put("endereco", aluno.getEndereco());
        valores.put("site", aluno.getSite());
        valores.put("nota", aluno.getNota());
        valores.put("caminhoFoto", aluno.getCaminhoFoto());

        db.update(tabela, valores, "id=?",new String[]{aluno.getId().toString()});

    }


    public boolean isAluno(String telefone)
    {
        String[] parametros = {telefone};

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT telefone FROM " + tabela + " WHERE telefone = ?", parametros);

        int total = cursor.getCount();
        cursor.close();
        return total > 0;
    }

}
