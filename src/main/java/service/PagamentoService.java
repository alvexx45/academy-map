package service;

import java.util.List;

import dao.PagamentoDAO;
import model.*;

public class PagamentoService {
    private PagamentoDAO pagamentoDAO;

    public PagamentoService() {
        pagamentoDAO = new PagamentoDAO();
    }

    public boolean adicionarPagamento(Pagamento pagamento) {
        return pagamentoDAO.adicionarPagamento(pagamento);
    }

    public List<Academia> getAcademiasPagas(int idCliente) {
        return pagamentoDAO.getAcademiasPagas(idCliente);
    }

    public List<Cliente> getClientesPagantes(int idAcademia) {
        return pagamentoDAO.getClientesPagantes(idAcademia);
    }

    public double somaPrecoFinal(int idAcademia) {
        return pagamentoDAO.somaPrecoFinal(idAcademia);
    }
}
