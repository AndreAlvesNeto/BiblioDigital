package br.edu.atitus.bibliodigital.repositories;

import java.util.ArrayList;
import java.util.List;
import br.edu.atitus.bibliodigital.entities.Livro;

public class LivroRepository {

    private List<Livro> livros = new ArrayList<>();

    public void adicionar(Livro livro) {
        livros.add(livro);
    }

    public List<Livro> listarTodos() {
        return livros;
    }

    public void remover(Livro livro) {
        livros.remove(livro);
    }

    public Livro buscarPorTituloExato(String titulo) {
        for (Livro livro : livros) {
            if (livro.getTitulo().equalsIgnoreCase(titulo)) {
                return livro;
            }
        }
        return null;
    }
}
    

