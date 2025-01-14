package service;

import java.util.List;

import dao.FavoritoDAO;
import model.Academia;

public class FavoritoService {
    private FavoritoDAO favoritoDAO;

    public FavoritoService() {
        favoritoDAO = new FavoritoDAO();
    }

    public boolean favoritarAcademia(int idCliente, int idAcademia) {
        return favoritoDAO.adicionarFavorito(idCliente, idAcademia);
    }

    public boolean desfavoritarAcademia(int idCliente, int idAcademia) {
        return favoritoDAO.removerFavorito(idCliente, idAcademia);
    }

    public List<Academia> getAcademiasFavoritadas(int idCliente) {
        return favoritoDAO.getAcademiasFavoritadas(idCliente);
    }
}
