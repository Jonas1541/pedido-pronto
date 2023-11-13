package pedidopronto.view;

import java.util.List;
import java.util.Scanner;

import pedidopronto.controller.MetodoPagamentoController;
import pedidopronto.controller.PedidoController;
import pedidopronto.controller.ProdutoController;
import pedidopronto.model.MetodoPagamento;
import pedidopronto.model.Pedido;
import pedidopronto.model.Produto;
import pedidopronto.repository.MetodoPagamentoRepository;
import pedidopronto.repository.PedidoRepository;
import pedidopronto.repository.ProdutoRepository;

public class PedidoView {

    public static void main(String[] args) {
        PedidoController pedidoController = new PedidoController(new PedidoRepository());
        ProdutoController produtoController = new ProdutoController(new ProdutoRepository());
        MetodoPagamentoController metodoPagamentoController = new MetodoPagamentoController(new MetodoPagamentoRepository());

        PedidoView pedidoView = new PedidoView(pedidoController, produtoController, metodoPagamentoController);
        pedidoView.realizarPedido();
    }

    private PedidoController pedidoController;
    private ProdutoController produtoController;
    private MetodoPagamentoController metodoPagamentoController;

    public PedidoView(PedidoController pedidoController, ProdutoController produtoController, MetodoPagamentoController metodoPagamentoController) {
        this.pedidoController = pedidoController;
        this.produtoController = produtoController;
        this.metodoPagamentoController = metodoPagamentoController;
    }

    public void realizarPedido() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("#### REALIZAR PEDIDO ####");

        // Listar produtos disponíveis
        List<Produto> produtosDisponiveis = produtoController.readProduto();
        exibirMenu(produtosDisponiveis);

        // Criar um pedido inicial
        Pedido pedido = new Pedido(true);

        do {
            System.out.print("Selecione o produto que deseja adicionar ao pedido: ");
            int escolha = scanner.nextInt();

            if (escolha >= 1 && escolha <= produtosDisponiveis.size()) {
                Produto produtoSelecionado = produtosDisponiveis.get(escolha - 1);
                pedido.addProduto(produtoSelecionado);

                System.out.println(produtoSelecionado.getNome() + " adicionado ao pedido");
            } else {
                System.out.println("Opção inválida. Por favor, selecione um produto válido.");
            }

            System.out.print("Deseja adicionar mais produtos? (S/N): ");
            char resposta = scanner.next().charAt(0);

            if (!Character.toString(resposta).equalsIgnoreCase("S")) {
                break;
            }

            exibirMenu(produtosDisponiveis);

        } while (true);

        // Selecionar ou criar método de pagamento
        MetodoPagamento metodoPagamento = selecionarOuCriarMetodoPagamento();


        

        // Passar método de pagamento e valor total para o construtor do pedido
        pedido.setTotal(calcularTotalPedido(pedido));
        pedido.setMetodoPagamento(metodoPagamento);

        exibirPedido(pedido);

        System.out.print("Deseja finalizar o pedido? (S/N): ");
        char finalizarPedido = scanner.next().charAt(0);

        if (!Character.toString(finalizarPedido).equalsIgnoreCase("S")) {
            System.out.println("Pedido cancelado.");
            return;
        }

        try {
            pedidoController.createPedido(pedido);
            System.out.println("Pedido finalizado com sucesso!");
        } catch (Exception e) {
            System.out.println("ERRO DETECTADO>>>>>>>: " + e);
        }
    }

    private void exibirMenu(List<Produto> produtos) {
        System.out.println("#MENU#");

        for (int i = 0; i < produtos.size(); i++) {
            Produto produto = produtos.get(i);
            System.out.println((i + 1) + " - " + produto.getNome());
        }
    }

    private void exibirPedido(Pedido pedido) {
        System.out.println("Produtos selecionados:");

        for (Produto produto : pedido.getListaProdutos()) {
            System.out.println("- " + produto.getNome());
        }

        System.out.println("Método de Pagamento: " + pedido.getMetodoPagamento().getNome());
        System.out.println("Total do Pedido: R$" + pedido.getTotal());
    }

    private MetodoPagamento selecionarOuCriarMetodoPagamento() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Método de Pagamento: ");
        String metodoPagamentoNome = scanner.next();

        MetodoPagamento metodoPagamento = metodoPagamentoController.findByName(metodoPagamentoNome);

        if (metodoPagamento == null) {
            // Se o método de pagamento não existir, crie um novo
            metodoPagamento = new MetodoPagamento(0, metodoPagamentoNome);
            metodoPagamentoController.createMetodoPagamento(metodoPagamento);
            System.out.println("Método de pagamento criado: " + metodoPagamentoNome);
        }
        
        return metodoPagamento;
    }

    private double calcularTotalPedido(Pedido pedido) {
        double total = 0.0;
        for (Produto produto : pedido.getListaProdutos()) {
            total += produto.getPreco();
        }
        return total;
    }
}
