package pedidopronto.view;

import java.util.Scanner;

import pedidopronto.controller.MetodoPagamentoController;
import pedidopronto.model.MetodoPagamento;
import pedidopronto.repository.MetodoPagamentoRepository;

public class MetodoPagamentoView {

    public static void main(String[] args) {
        MetodoPagamentoView metodoPagamentoView = new MetodoPagamentoView();
        metodoPagamentoView.cadastrarMetodoPagamento();
    }

    public void cadastrarMetodoPagamento() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("### CADASTRO DE MÉTODOS DE PAGAMENTO ###");

        System.out.print("Nome do método de pagamento: ");
        String nomeMetodo = scanner.nextLine();


        MetodoPagamento novoMetodoPagamento = new MetodoPagamento(2, nomeMetodo);

        try {
            MetodoPagamentoController metodoPagamentoController = new MetodoPagamentoController(new MetodoPagamentoRepository());
            metodoPagamentoController.createMetodoPagamento(novoMetodoPagamento);
            System.out.println("Método de pagamento cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("ERRO DETECTADO>>>>>>>: " + e);
        }
    }
}
