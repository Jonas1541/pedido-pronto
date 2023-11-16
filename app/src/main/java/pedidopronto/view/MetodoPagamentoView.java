package pedidopronto.view;

import java.util.List;
import java.util.Scanner;

import pedidopronto.controller.MetodoPagamentoController;
import pedidopronto.model.MetodoPagamento;
import pedidopronto.repository.MetodoPagamentoRepository;

public class MetodoPagamentoView {

    private MetodoPagamentoController metodoPagamentoController;

    public MetodoPagamentoView() {
        metodoPagamentoController = new MetodoPagamentoController(new MetodoPagamentoRepository());
    }

    public void cadastrarMetodoPagamento() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("### CADASTRO DE MÉTODOS DE PAGAMENTO ###");
            System.out.print("Nome do método de pagamento: ");
            String nomeMetodo = scanner.nextLine();

            MetodoPagamento novoMetodoPagamento = new MetodoPagamento(nomeMetodo);
            metodoPagamentoController.createMetodoPagamento(novoMetodoPagamento);
            System.out.println("Método de pagamento cadastrado com sucesso!");
        }
    }

    public void lerMetodoPagamento() {
        System.out.println("### LISTA DE MÉTODOS DE PAGAMENTO ###");
        List<MetodoPagamento> metodos = metodoPagamentoController.readMetodoPagamento();
        metodos.forEach(metodo -> System.out.println(metodo.getId() + " - " + metodo.getNome()));
    }

    public void editarMetodoPagamento() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("### EDITAR MÉTODO DE PAGAMENTO ###");
            System.out.print("ID do método de pagamento para editar: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            System.out.print("Novo nome do método de pagamento: ");
            String novoNome = scanner.nextLine();

            MetodoPagamento metodoPagamento = new MetodoPagamento(id, novoNome);
            metodoPagamentoController.updateMetodoPagamento(metodoPagamento);
            System.out.println("Método de pagamento atualizado com sucesso!");
        }
    }

    public void deletarMetodoPagamento() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("### DELETAR MÉTODO DE PAGAMENTO ###");
            System.out.print("ID do método de pagamento para deletar: ");
            int id = scanner.nextInt();

            metodoPagamentoController.deleteMetodoPagamento(id);
            System.out.println("Método de pagamento deletado com sucesso!");
        }
    }
}
