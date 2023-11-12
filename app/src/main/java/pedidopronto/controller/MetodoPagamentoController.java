package pedidopronto.controller;

import java.util.List;

import pedidopronto.model.MetodoPagamento;
import pedidopronto.repository.MetodoPagamentoRepository;

public class MetodoPagamentoController {
    private MetodoPagamentoRepository metodoPagamentoRepository;

    public MetodoPagamentoController(MetodoPagamentoRepository metodoPagamentoRepository) {
        this.metodoPagamentoRepository = metodoPagamentoRepository;
    }

    public void createMetodoPagamento(MetodoPagamento metodoPagamento) {
        metodoPagamentoRepository.create(metodoPagamento);
    }

    public List<MetodoPagamento> readMetodoPagamento() {
        return metodoPagamentoRepository.read();
    }

    public void updateMetodoPagamento(MetodoPagamento metodoPagamento) {
        metodoPagamentoRepository.update(metodoPagamento);
    }

    public void deleteMetodoPagamento(int id) {
        metodoPagamentoRepository.delete(id);
    }

    public MetodoPagamento findByName(String nome){
        return metodoPagamentoRepository.findByName(nome);
    }
}
