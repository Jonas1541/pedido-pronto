package pedidopronto.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pedidopronto.controller.MetodoPagamentoController;
import pedidopronto.controller.PedidoController;
import pedidopronto.controller.ProdutoController;
import pedidopronto.model.MetodoPagamento;
import pedidopronto.model.Pedido;
import pedidopronto.model.Produto;
import pedidopronto.repository.MetodoPagamentoRepository;
import pedidopronto.repository.ProdutoRepository;

public class PedidoView {

    public static void main(String[] args) {
        PedidoView pedidoView = new PedidoView();
        pedidoView.realizarPedido();
    }

    public void realizarPedido() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("### Realizar Pedido ###");

        // Lista de produtos disponíveis
        List<Produto> produtosDisponiveis = obterProdutosDisponiveis();

        // Exibir produtos disponíveis
        exibirProdutos(produtosDisponiveis);

        // Criar pedido
        Pedido pedido = new Pedido();
        pedido.setProdutos(selecionarProdutos(produtosDisponiveis));
        pedido.setMetodoPagamento(selecionarMetodoPagamento());

        // Aqui você precisaria chamar métodos para interagir com a camada de modelo e persistência (Model e Repository)
        // Por exemplo, criar um objeto Pedido e enviá-lo para o repository

        try {
            PedidoController pedidoController = new PedidoController();
            pedidoController.createPedido(pedido);
            System.out.println("Pedido realizado com sucesso!");
        } catch (Exception e) {
            System.out.println("ERRO DETECTADO>>>>>>>: " + e);
        }
    }

    private List<Produto> obterProdutosDisponiveis() {
        ProdutoController produtoController = new ProdutoController(new ProdutoRepository());
        return produtoController.listarProdutos();
    }

    private void exibirProdutos(List<Produto> produtos) {
        System.out.println("### Produtos Disponíveis ###");
        for (Produto produto : produtos) {
            System.out.println(produto.getId() + ". " + produto.getNome() + " - R$" + produto.getPreco());
        }
    }

    private List<Produto> selecionarProdutos(List<Produto> produtosDisponiveis) {
        List<Produto> produtosSelecionados = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Selecione os produtos pelo ID (separados por vírgula):");
        String inputProdutos = scanner.nextLine();
        String[] idsProdutos = inputProdutos.split(",");

        for (String idProduto : idsProdutos) {
            try {
                int id = Integer.parseInt(idProduto.trim());
                Produto produto = buscarProdutoPorId(produtosDisponiveis, id);
                if (produto != null) {
                    produtosSelecionados.add(produto);
                }
            } catch (NumberFormatException e) {
                // Lidar com a entrada inválida (não numérica)
                System.out.println("ID inválido: " + idProduto);
            }
        }

        return produtosSelecionados;
    }

    private Produto buscarProdutoPorId(List<Produto> produtos, int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        // Retornar null se o produto não for encontrado
        return null;
    }

    private MetodoPagamento selecionarMetodoPagamento() {
        Scanner scanner = new Scanner(System.in);

        // Lista de métodos de pagamento disponíveis
        MetodoPagamentoController metodoPagamentoController = new MetodoPagamentoController(new MetodoPagamentoRepository());
        List<MetodoPagamento> metodosPagamento = metodoPagamentoController.readMetodoPagamento();

        // Exibir métodos de pagamento disponíveis
        exibirMetodosPagamento(metodosPagamento);

        // Solicitar a seleção do método de pagamento
        System.out.println("Selecione o método de pagamento pelo ID:");
        int idMetodoPagamento = scanner.nextInt();

        // Buscar o método de pagamento selecionado
        return buscarMetodoPagamentoPorId(metodosPagamento, idMetodoPagamento);
    }

    private void exibirMetodosPagamento(List<MetodoPagamento> metodosPagamento) {
        System.out.println("### Métodos de Pagamento Disponíveis ###");
        for (MetodoPagamento metodoPagamento : metodosPagamento) {
            System.out.println(metodoPagamento.getId() + ". " + metodoPagamento.getNome());
        }
    }

    private MetodoPagamento buscarMetodoPagamentoPorId(List<MetodoPagamento> metodosPagamento, int id) {
        for (MetodoPagamento metodoPagamento : metodosPagamento) {
            if (metodoPagamento.getId() == id) {
                return metodoPagamento;
            }
        }
        // Retornar null se o método de pagamento não for encontrado
        return null;
    }
}
