package br.com.caelum.cadastro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by android6017 on 01/06/16.
 */
public class Prova implements Serializable {

    private String data;
    private String materia;
    private String descricao;
    private List<String> topicos = new ArrayList<String>();


    public Prova(String data, String materia) {
        this.data = data;
        this.materia = materia;
    }

    @Override
    public String toString()

    {
        return materia;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getTopicos() {
        return topicos;
    }

    public void setTopicos(List<String> topicos) {
        this.topicos = topicos;
    }
}
