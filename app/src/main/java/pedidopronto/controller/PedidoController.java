package pedidopronto.controller;

import java.util.List;

import pedidopronto.model.Pedido;
import pedidopronto.repository.PedidoRepository;

public class PedidoController {
    private PedidoRepository pedidoRepository;

    public PedidoController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public void createPedido(Pedido pedido) {
        pedidoRepository.create(pedido);
    }

    public List<Pedido> readPedido() {
        return pedidoRepository.read();
    }

    public void updatePedido(Pedido pedido) {
        pedidoRepository.update(pedido);
    }

    public void deletePedido(int id) {
        pedidoRepository.delete(id);
    }

    public double totalValue(int id){
        return pedidoRepository.totalValue(id);
    }
}
