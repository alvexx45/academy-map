package dao;

import model.Academia;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AcademiaDAO {
    private Connection connection;

    public AcademiaDAO() {
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

    public void insert(Academia academia) {
        String sql = "INSERT INTO academia (nome_academia, localizacao, destaque, email, senha, cnpj, preco_aula, foto_perfil) VALUES (?, ?, false, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, academia.getNomeAcademia());
            stmt.setString(2, academia.getLocalizacao());
            stmt.setString(3, academia.getEmail());
            stmt.setString(4, academia.getSenha());
            stmt.setString(5, academia.getCnpj());
            stmt.setFloat(6, academia.getPrecoAula());
            stmt.setString(7, academia.getFotoPerfil());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Academia getByEmailAndPassword(String email, String senha) {
        Academia academia = null;
        String sql = "SELECT * FROM academia WHERE email = ? AND senha = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                academia = new Academia(
                        rs.getInt("id_academia"),
                        rs.getString("nome_academia"),
                        rs.getString("localizacao"),
                        rs.getBoolean("destaque"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("cnpj"),
                        rs.getFloat("preco_aula"),
                        rs.getString("foto_perfil"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return academia;
    }

    public List<Academia> getAllAcademias() {
        List<Academia> academias = new ArrayList<>();
        String sql = "SELECT * FROM academia";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Academia academia = new Academia(
                        rs.getInt("id_academia"),
                        rs.getString("nome_academia"),
                        rs.getString("localizacao"),
                        rs.getBoolean("destaque"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("cnpj"),
                        rs.getFloat("preco_aula"),
                        rs.getString("foto_perfil"));
                academias.add(academia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return academias;
    }

    public Academia getAcademiaById(int id) {
        Academia academia = null;
        String sql = "SELECT * FROM academia WHERE id_academia = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                academia = new Academia(
                        rs.getInt("id_academia"),
                        rs.getString("nome_academia"),
                        rs.getString("localizacao"),
                        rs.getBoolean("destaque"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("cnpj"),
                        rs.getFloat("preco_aula"),
                        rs.getString("foto_perfil"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return academia;
    }

    public void updateAcademia(Academia academia) {
        String sql = "UPDATE academia SET nome_academia = ?, localizacao = ?, destaque = ?, email = ?, senha = ?, cnpj = ?, preco_aula = ?, foto_perfil = ? WHERE id_academia = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, academia.getNomeAcademia());
            stmt.setString(2, academia.getLocalizacao());
            stmt.setBoolean(3, academia.getDestaque());
            stmt.setString(4, academia.getEmail());
            stmt.setString(5, academia.getSenha()); // Considere usar um hash para a senha
            stmt.setString(6, academia.getCnpj());
            stmt.setFloat(7, academia.getPrecoAula());
            stmt.setString(8, academia.getFotoPerfil());
            stmt.setInt(9, academia.getIdAcademia());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Aqui você pode lançar uma RuntimeException ou um erro mais específico
            throw new RuntimeException("Erro ao atualizar academia", e); // Lançar uma exceção
        }
    }

    public void deleteAcademia(int id) {
        String sql = "DELETE FROM academia WHERE id_academia = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void realizarPagamento(int academiaId) {
        String sql = "UPDATE academia SET destaque = true WHERE id_academia = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, academiaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao realizar pagamento e destacar academia", e);
        }
    }

    public Academia getByEmail(String email) {
        Academia academia = null;
        String sql = "SELECT * FROM academia WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                academia = new Academia(
                        rs.getInt("id_academia"),
                        rs.getString("nome_academia"),
                        rs.getString("localizacao"),
                        rs.getBoolean("destaque"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("cnpj"),
                        rs.getFloat("preco_aula"),
                        rs.getString("foto_perfil"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return academia;
    }
}