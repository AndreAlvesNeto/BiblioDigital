package br.edu.atitus.bibliodigital.views;

import java.util.List;
import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.services.LivroService;

public class BiblioDigitalApplication { public static void main(String[] args) {
        LivroService service = new LivroService();
        int opcao = -1;

        IO.println("===== Bem vindo ao BiblioDigital =====");

        while (opcao != 0) {
            IO.println("\nMenu:");
            IO.println("1 - Cadastrar novo Livro");
            IO.println("2 - Pesquisar Livro por título");
            IO.println("3 - Pesquisar Livro por autor");
            IO.println("4 - Alterar dados do Livro");
            IO.println("5 - Excluir Livro");
        IO.println("0 - Sair");
            IO.println("\nEscolha uma das opções acima:");

                       try {
                opcao = Integer.parseInt(IO.readln().trim());
            } catch (NumberFormatException e) {
                opcao = -1;
            }
            switch (opcao) {
                case 1:
                    cadastrar(service);
                    break;
                case 2:
                    pesquisarPorTitulo(service);
                    break;
                case 3:
                    pesquisarPorAutor(service);
                    break;
                case 4:
                    alterar(service);
                    break;
                case 5:
                    excluir(service);
                    break;
                case 0:
                    IO.println("Saindo...");
                    break;
                default:
                    IO.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void cadastrar(LivroService service) {
        IO.println("Tipo do livro: 1 - Físico, 2 - Digital, 3 - Áudio");
        String tipo = IO.readln().trim();

        try {
            String titulo = IO.readln("Título: ").trim();
            String autor = IO.readln("Autor: ").trim();
            int ano = Integer.parseInt(IO.readln("Ano de publicação: ").trim());

            Livro livro;

            if (tipo.equals("1")) {
                int quantidade = Integer.parseInt(IO.readln("Quantidade de exemplares: ").trim());
                String localizacao = IO.readln("Localização: ").trim();
                livro = new LivroFisico(titulo, autor, ano, quantidade, localizacao);
            } else if (tipo.equals("2")) {
                String formato = IO.readln("Formato (PDF, EPUB...): ").trim();
                double tamanho = Double.parseDouble(IO.readln("Tamanho do arquivo (MB): ").trim());
                livro = new LivroDigital(titulo, autor, ano, formato, tamanho);
            } else if (tipo.equals("3")) {
                int duracao = Integer.parseInt(IO.readln("Duração (minutos): ").trim());
                String narrador = IO.readln("Narrador: ").trim();
                livro = new AudioLivro(titulo, autor, ano, duracao, narrador);
            } else {
                IO.println("Tipo inválido.");
                return;
            }

            service.cadastrar(livro);
            IO.println("Livro cadastrado com sucesso!");

        } catch (NumberFormatException e) {
            IO.println("Erro: digite um número válido.");
        } catch (IllegalArgumentException e) {
            IO.println("Erro: " + e.getMessage());
        }
    }

    private static void pesquisarPorTitulo(LivroService service) {
        String texto = IO.readln("Digite o título (ou parte dele): ");
        List<Livro> encontrados = service.pesquisarPorTitulo(texto);

        if (encontrados.isEmpty()) {
            IO.println("Nenhum livro encontrado para a pesquisa realizada.");
        } else {
            IO.println("\n===== Resultados =====");
            for (Livro livro : encontrados) {
                IO.println(livro);
            }
        }
    }

    private static void pesquisarPorAutor(LivroService service) {
        String texto = IO.readln("Digite o autor (ou parte dele): ");
        List<Livro> encontrados = service.pesquisarPorAutor(texto);

        if (encontrados.isEmpty()) {
            IO.println("Nenhum livro encontrado para o autor informado.");
        } else {
            IO.println("\n===== Resultados =====");
            for (Livro livro : encontrados) {
                IO.println(livro);
            }
        }
    }

    private static void alterar(LivroService service) {
        if (!listarLivros(service)) {
            return;
        }

        try {
            int indice = Integer.parseInt(IO.readln("Informe o índice do livro que deseja alterar: ").trim());
            Livro livro = service.buscarPorIndice(indice);

            IO.println("\nDados atuais:");
            IO.println(livro);
            IO.println("\nInforme os novos dados:");

            String titulo = IO.readln("Título: ").trim();
            String autor = IO.readln("Autor: ").trim();
            int ano = Integer.parseInt(IO.readln("Ano de publicação: ").trim());

            service.alterar(livro, titulo, autor, ano);

                        if (livro instanceof LivroFisico) {
                LivroFisico fisico = (LivroFisico) livro;
                fisico.setQuantidadeExemplares(Integer.parseInt(IO.readln("Quantidade de exemplares: ").trim()));
                fisico.setLocalizacao(IO.readln("Localização: ").trim());
            } else if (livro instanceof LivroDigital) {
                LivroDigital digital = (LivroDigital) livro;
                digital.setFormato(IO.readln("Formato: ").trim());
                digital.setTamanhoArquivo(Double.parseDouble(IO.readln("Tamanho do arquivo (MB): ").trim()));
            } else if (livro instanceof AudioLivro) {
                AudioLivro audio = (AudioLivro) livro;
                audio.setDuracao(Integer.parseInt(IO.readln("Duração (minutos): ").trim()));
                audio.setNarrador(IO.readln("Narrador: ").trim());
            }

            IO.println("Livro alterado com sucesso!");

        } catch (NumberFormatException e) {
            IO.println("Erro: digite um número válido.");
        } catch (IllegalArgumentException e) {
            IO.println("Erro: " + e.getMessage());
        }
    }

    private static boolean listarLivros(LivroService service) {
        List<Livro> livros = service.listarTodos();

        if (livros.isEmpty()) {
            IO.println("Nenhum livro cadastrado.");
            return false;
        }

        IO.println("\n===== Livros cadastrados =====");
        for (int i = 0; i < livros.size(); i++) {
            IO.println((i + 1) + " - " + livros.get(i).getTitulo());
        }
        return true;
    }

    private static void excluir(LivroService service) {
        if (!listarLivros(service)) {
            return;
        }

        try {
            int indice = Integer.parseInt(IO.readln("Informe o índice do livro que deseja excluir: ").trim());
            Livro livro = service.buscarPorIndice(indice);

            String resposta = IO.readln("Excluir \"" + livro.getTitulo() + "\"? (S/N): ").trim();

            if (resposta.equalsIgnoreCase("S")) {
                service.excluir(livro);
                IO.println("Livro excluído com sucesso!");
            } else {
                IO.println("Exclusão cancelada.");
            }
        } catch (NumberFormatException e) {
            IO.println("Erro: digite um número válido.");
        } catch (IllegalArgumentException e) {
            IO.println("Erro: " + e.getMessage());
        }
    }
}