package pedidopronto.view;

import java.util.List;
import java.util.Scanner;

import pedidopronto.controller.MetodoPagamentoController;
import pedidopronto.controller.PedidoController;
import pedidopronto.controller.ProdutoController;
import pedidopronto.model.ItemPedido;
import pedidopronto.model.MetodoPagamento;
import pedidopronto.model.Pedido;
import pedidopronto.model.Produto;
import pedidopronto.repository.MetodoPagamentoRepository;
import pedidopronto.repository.PedidoRepository;
import pedidopronto.repository.ProdutoRepository;

public class PedidoView {

    private PedidoController pedidoController;
    private ProdutoController produtoController;
    private MetodoPagamentoController metodoPagamentoController;

    public PedidoView(PedidoController pedidoController, ProdutoController produtoController,
            MetodoPagamentoController metodoPagamentoController) {
        this.pedidoController = pedidoController;
        this.produtoController = produtoController;
        this.metodoPagamentoController = metodoPagamentoController;
    }

    public void criarPedido() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("#### REALIZAR PEDIDO ####");
            List<Produto> produtosDisponiveis = produtoController.readProduto();
            exibirMenu(produtosDisponiveis);

            Pedido pedido = new Pedido(true);

            do {
                System.out.print("Selecione o produto que deseja adicionar ao pedido: ");
                int escolha = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer do scanner

                if (escolha >= 1 && escolha <= produtosDisponiveis.size()) {
                    Produto produtoSelecionado = produtosDisponiveis.get(escolha - 1);
                    System.out.print("Quantidade do produto: ");
                    int quantidade = scanner.nextInt();

                    ItemPedido itemPedido = new ItemPedido(produtoSelecionado, quantidade);
                    pedido.addItemPedido(itemPedido);

                    System.out.println(produtoSelecionado.getNome() + " adicionado ao pedido.");
                } else {
                    System.out.println("Opção inválida. Por favor, selecione um produto válido.");
                }

                System.out.print("Deseja adicionar mais produtos? (S/N): ");
                char resposta = scanner.next().charAt(0);

                if (!Character.toString(resposta).equalsIgnoreCase("S")) {
                    break;
                }

            } while (true);

            MetodoPagamento metodoPagamento = selecionarOuCriarMetodoPagamento(scanner);
            pedido.setTotal(calcularTotalPedido(pedido));
            pedido.setMetodoPagamento(metodoPagamento);

            exibirPedido(pedido);

            System.out.print("Deseja finalizar o pedido? (S/N): ");
            char finalizarPedido = scanner.next().charAt(0);

            if (!Character.toString(finalizarPedido).equalsIgnoreCase("S")) {
                System.out.println("Pedido cancelado.");
                return;
            }

            pedidoController.createPedido(pedido);
            System.out.println("Pedido finalizado com sucesso!");
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
        for (ItemPedido item : pedido.getListaItensPedidos()) {
            System.out.println("- " + item.getProduto().getNome() + " (Quantidade: " + item.getQuantidade() + ")");
        }
        System.out.println("Método de Pagamento: " + pedido.getMetodoPagamento().getNome());
        System.out.println("Total do Pedido: R$" + pedido.getTotal());
    }

    private MetodoPagamento selecionarOuCriarMetodoPagamento(Scanner scanner) {
        System.out.print("Método de Pagamento: ");
        String metodoPagamentoNome = scanner.next();

        MetodoPagamento metodoPagamento = metodoPagamentoController.findByName(metodoPagamentoNome);

        if (metodoPagamento == null) {
            metodoPagamento = new MetodoPagamento(0, metodoPagamentoNome);
            metodoPagamentoController.createMetodoPagamento(metodoPagamento);
            System.out.println("Método de pagamento criado: " + metodoPagamentoNome);
        }

        return metodoPagamento;
    }

    private double calcularTotalPedido(Pedido pedido) {
        double total = 0.0;
        for (ItemPedido item : pedido.getListaItensPedidos()) {
            total += item.getProduto().getPreco() * item.getQuantidade();
        }
        return total;
    }

    public void lerPedidos() {
        System.out.println("### LISTA DE PEDIDOS ###");
        List<Pedido> pedidos = pedidoController.readPedido();
        for (Pedido pedido : pedidos) {
            System.out.println("Pedido ID: " + pedido.getId() + " - Total: R$" + pedido.getTotal());
            System.out.println("Produtos no Pedido:");
            for (ItemPedido item : pedido.getListaItensPedidos()) {
                System.out.println("\tProduto: " + item.getProduto().getNome() + " - Quantidade: " + item.getQuantidade());
            }
            System.out.println("Método de Pagamento: " + pedido.getMetodoPagamento().getNome());
            System.out.println("-------");
        }
    }
    

    public void editarPedido() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("### EDITAR PEDIDO ###");
            System.out.print("ID do pedido para editar: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
    
            Pedido pedido = pedidoController.findById(id);
            if (pedido == null) {
                System.out.println("Pedido não encontrado.");
                return;
            }
    
            // Exibir informações atuais do pedido
            System.out.println("Pedido Atual:");
            exibirPedido(pedido);
    
            // Alterar método de pagamento
            System.out.print("Alterar método de pagamento? (S/N): ");
            if (scanner.next().equalsIgnoreCase("S")) {
                MetodoPagamento novoMetodoPagamento = selecionarOuCriarMetodoPagamento(scanner);
                pedido.setMetodoPagamento(novoMetodoPagamento);
            }
            scanner.nextLine(); // Limpar buffer
    
            // Lógica para alterar itens do pedido
            System.out.print("Deseja alterar os produtos do pedido? (S/N): ");
            if (scanner.next().equalsIgnoreCase("S")) {
                pedido.getListaItensPedidos().clear(); // Limpar itens existentes
                List<Produto> produtosDisponiveis = produtoController.readProduto();
                do {
                    exibirMenu(produtosDisponiveis);
                    System.out.print("Selecione o produto para adicionar ao pedido: ");
                    int escolhaProduto = scanner.nextInt();
                    scanner.nextLine(); // Limpar buffer
    
                    if (escolhaProduto >= 1 && escolhaProduto <= produtosDisponiveis.size()) {
                        Produto produtoSelecionado = produtosDisponiveis.get(escolhaProduto - 1);
                        System.out.print("Quantidade do produto: ");
                        int quantidade = scanner.nextInt();
                        scanner.nextLine(); // Limpar buffer
    
                        ItemPedido itemPedido = new ItemPedido(produtoSelecionado, quantidade);
                        pedido.addItemPedido(itemPedido);
    
                        System.out.println(produtoSelecionado.getNome() + " adicionado ao pedido.");
                    } else {
                        System.out.println("Opção inválida. Por favor, selecione um produto válido.");
                    }
    
                    System.out.print("Deseja adicionar mais produtos? (S/N): ");
                } while (scanner.next().equalsIgnoreCase("S"));
            }
    
            // Atualizar o total do pedido e salvar as alterações
            pedido.setTotal(calcularTotalPedido(pedido));
            pedidoController.updatePedido(pedido);
            System.out.println("Total do Pedido: R$" + pedido.getTotal());
            System.out.println("Pedido atualizado com sucesso!");
        }
    }
    

    public void deletarPedido() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("### DELETAR PEDIDO ###");
            System.out.print("ID do pedido para deletar: ");
            int id = scanner.nextInt();
            pedidoController.deletePedido(id);
            System.out.println("Pedido deletado com sucesso!");
        }
    }
}
