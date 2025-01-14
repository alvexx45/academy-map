package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import model.*;

public class PagamentoDAO {
    private Connection connection;

    public PagamentoDAO() {
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

    public boolean adicionarPagamento(Pagamento pagamento) {
        // Consulta para obter o preço da aula da academia
        String precoSql = "SELECT preco_aula FROM academia WHERE id_academia = ?";
        try (PreparedStatement precoStmt = connection.prepareStatement(precoSql)) {
            precoStmt.setInt(1, pagamento.getAcademia().getIdAcademia());
            ResultSet rs = precoStmt.executeQuery();

            if (rs.next()) {
                float precoAula = rs.getFloat("preco_aula");
                // Calcula o preço final
                double precoFinal = precoAula * pagamento.getQuantidadeAulas();
                pagamento.setPrecoFinal(precoFinal);

                // Agora insira o pagamento
                String sql = "INSERT INTO pagamento (id_academia, id_cliente, quantidade_aulas, preco_final) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, pagamento.getAcademia().getIdAcademia());
                    stmt.setInt(2, pagamento.getCliente().getId());
                    stmt.setInt(3, pagamento.getQuantidadeAulas());
                    stmt.setDouble(4, precoFinal); // Usa o preço final calculado
                    stmt.executeUpdate();
                    return true;
                }
            } else {
                System.out.println("Academia não encontrada.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Academia> getAcademiasPagas(int idCliente) {
        List<Academia> academias = new ArrayList<>();
        String sql = "SELECT a.id_academia, a.nome_academia, a.localizacao, a.preco_aula, a.foto_perfil " +
                "FROM pagamento f JOIN academia a ON f.id_academia = a.id_academia " +
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

    public List<Cliente> getClientesPagantes(int idAcademia) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.id_cliente, c.nome_cliente, c.cpf " +
                "FROM pagamento f JOIN cliente c ON f.id_cliente = c.id_cliente " +
                "WHERE f.id_academia = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAcademia);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("nome_cliente"));
                cliente.setCpf(rs.getString("cpf"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public double somaPrecoFinal(int idAcademia) {
        double precoFinal = 0;
        String sql = "SELECT SUM(preco_final) AS total FROM pagamento WHERE id_academia = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAcademia);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                precoFinal = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return precoFinal;
    }
}
