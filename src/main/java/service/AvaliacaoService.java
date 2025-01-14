package service;

import java.util.List;

import dao.AvaliacaoDAO;
import model.Avaliacao;

public class AvaliacaoService {
    private AvaliacaoDAO avaliacaoDAO;

    // Construtor que inicializa o DAO de avaliação
    public AvaliacaoService() {
        avaliacaoDAO = new AvaliacaoDAO();
    }

    public boolean adicionarAvaliacao(Avaliacao avaliacao) {
        return avaliacaoDAO.adicionarAvaliacao(avaliacao);
    }

    // Retorna todas as avaliações de uma academia específica
    public List<Avaliacao> getAvaliacoesPorAcademia(int idAcademia) {
        return avaliacaoDAO.getAvaliacoesPorAcademia(idAcademia);
    }
}
