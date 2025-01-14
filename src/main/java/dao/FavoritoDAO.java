package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import model.Academia;

public class FavoritoDAO {

    private Connection connection;

    public FavoritoDAO() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Desculpe, não foi possível encontrar config.properties");
                return;
            }

            // Carrega as propriedades do arquivo
            Properties props = new Properties();
            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            // Conecta ao banco de dados usando os dados do arquivo
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }

    }

    public boolean adicionarFavorito(int idCliente, int idAcademia) {
        String sql = "INSERT INTO favoritos (id_cliente, id_academia) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setInt(2, idAcademia);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removerFavorito(int idCliente, int idAcademia) {
        String sql = "DELETE FROM favoritos WHERE id_cliente = ? AND id_academia = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setInt(2, idAcademia);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Academia> getAcademiasFavoritadas(int idCliente) {
        List<Academia> academias = new ArrayList<>();
        String sql = "SELECT a.id_academia, a.nome_academia, a.localizacao, a.preco_aula, a.foto_perfil " +
                "FROM favoritos f JOIN academia a ON f.id_academia = a.id_academia " +
                "WHERE f.id_cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Academia academia = new Academia();
                academia.setIdAcademia(rs.getInt("id_academia"));
                academia.setNomeAcademia(rs.getString("nome_academia"));
                academia.setLocalizacao(rs.getString("localizacao"));
                academia.setPrecoAula(rs.getFloat("preco_aula"));
                academia.setFotoPerfil(rs.getString("foto_perfil"));
                academias.add(academia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return academias;
    }
}
