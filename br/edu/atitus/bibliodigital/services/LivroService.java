package br.edu.atitus.bibliodigital.services;

import java.util.ArrayList;
import java.util.List;

import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.repositories.LivroRepository;

public class LivroService {

    private LivroRepository repository = new LivroRepository();

    private void validar(String titulo, String autor, int ano) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ser vazio.");
        }

        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("O autor não pode ser vazio.");
        }

        if (ano < 1900) {
            throw new IllegalArgumentException("O ano não pode ser menor que 1900.");
        }
    }

    public void cadastrar(Livro livro) {
        validar(livro.getTitulo(), livro.getAutor(), livro.getAnoPublicacao());

        Livro existente = repository.buscarPorTituloExato(livro.getTitulo().trim());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe um livro com esse título.");
        }

        livro.setTitulo(livro.getTitulo().trim());
        livro.setAutor(livro.getAutor().trim());

        repository.adicionar(livro);
    }

    public void alterar(Livro livro, String titulo, String autor, int ano) {
        validar(titulo, autor, ano);

        Livro existente = repository.buscarPorTituloExato(titulo.trim());
        if (existente != null && existente != livro) {
            throw new IllegalArgumentException("Já existe um livro com esse título.");
        }

        livro.setTitulo(titulo.trim());
        livro.setAutor(autor.trim());
        livro.setAnoPublicacao(ano);
    }

    public List<Livro> pesquisarPorTitulo(String texto) {
        List<Livro> resultado = new ArrayList<>();

        for (Livro livro : repository.listarTodos()) {
            if (livro.getTitulo().toLowerCase().contains(texto.toLowerCase().trim())) {
                resultado.add(livro);
            }
        }

        return resultado;
    }

    public List<Livro> pesquisarPorAutor(String texto) {
        List<Livro> resultado = new ArrayList<>();

        for (Livro livro : repository.listarTodos()) {
            if (livro.getAutor().toLowerCase().contains(texto.toLowerCase().trim())) {
                resultado.add(livro);
            }
        }

        return resultado;
    }

    public List<Livro> listarTodos() {
        return repository.listarTodos();
    }

    public Livro buscarPorIndice(int indice) {
        List<Livro> livros = repository.listarTodos();

        if (indice < 1 || indice > livros.size()) {
            throw new IllegalArgumentException("Índice inválido.");
        }

        return livros.get(indice - 1);
    }

    public void excluir(Livro livro) {
        repository.remover(livro);
    }
}