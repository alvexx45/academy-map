package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import model.Academia;
import model.Avaliacao;

public class AvaliacaoDAO {

    private Connection connection;

    // Construtor que inicializa a conexão com o banco de dados
    public AvaliacaoDAO() {
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

    // Método para adicionar uma avaliação
    public boolean adicionarAvaliacao(Avaliacao avaliacao) {
        String sql = "INSERT INTO avaliacao (nota, comentario, id_academia) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, avaliacao.getNota());
            stmt.setString(2, avaliacao.getComentario());
            // Supondo que a academia está associada ao objeto Avaliacao
            stmt.setInt(3, avaliacao.getAcademia().getIdAcademia());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar todas as avaliações de uma academia específica
    public List<Avaliacao> getAvaliacoesPorAcademia(int idAcademia) {
        List<Avaliacao> avaliacoes = new ArrayList<>();
        String sql = "SELECT id_avaliacao, nota, comentario FROM avaliacao WHERE id_academia = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAcademia);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setIdAvaliacao(rs.getInt("id_avaliacao"));
                avaliacao.setNota(rs.getInt("nota"));
                avaliacao.setComentario(rs.getString("comentario"));

                // Relaciona a academia à avaliação
                Academia academia = new Academia();
                academia.setIdAcademia(idAcademia);
                avaliacao.setAcademia(academia);

                avaliacoes.add(avaliacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avaliacoes;
    }
}
