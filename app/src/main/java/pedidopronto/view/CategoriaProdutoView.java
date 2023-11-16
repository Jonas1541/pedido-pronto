package pedidopronto.view;

import java.util.List;
import java.util.Scanner;

import pedidopronto.controller.CategoriaProdutoController;
import pedidopronto.model.CategoriaProduto;
import pedidopronto.repository.CategoriaProdutoRepository;

public class CategoriaProdutoView {

    private CategoriaProdutoController categoriaController;

    public CategoriaProdutoView() {
        categoriaController = new CategoriaProdutoController(new CategoriaProdutoRepository());
    }

    public void cadastrarCategoria() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("### CADASTRO DE CATEGORIA ###");
        System.out.print("Nome da categoria: ");
        String nomeCategoria = scanner.nextLine();
        scanner.close();
        CategoriaProduto novaCategoria = new CategoriaProduto(nomeCategoria);
        categoriaController.createCategoriaProduto(novaCategoria);
        System.out.println("Categoria cadastrada com sucesso!");
    }

    public void lerCategoria() {
        System.out.println("### LISTA DE CATEGORIAS ###");
        List<CategoriaProduto> categorias = categoriaController.readCategoriaProduto();
        categorias.forEach(categoria -> System.out.println(categoria.getId() + " - " + categoria.getCategoria()));
    }

    public void editarCategoria() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("### EDITAR CATEGORIA ###");
        System.out.print("ID da categoria para editar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer
        System.out.print("Novo nome da categoria: ");
        String novoNome = scanner.nextLine();
        scanner.close();
        CategoriaProduto categoria = new CategoriaProduto(id, novoNome);
        categoriaController.updateCategoriaProduto(categoria);
        System.out.println("Categoria atualizada com sucesso!");
    }

    public void deletarCategoria() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("### DELETAR CATEGORIA ###");
        System.out.print("ID da categoria para deletar: ");
        int id = scanner.nextInt();
        scanner.close();
        categoriaController.deleteCategoriaProduto(id);
        System.out.println("Categoria deletada com sucesso!");
    }
}
