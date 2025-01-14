package dao;

import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class ClienteDAO {

    private Connection connection;

    public ClienteDAO() {
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

    public void insert(Cliente cliente) {
        String query = "INSERT INTO cliente (nome_cliente, email_cliente, cpf, senha, ativo) VALUES (?, ?, ?, ?, true)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getSenha());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cliente getByEmailAndPassword(String email, String senha) {
        Cliente cliente = null;
        String query = "SELECT * FROM cliente WHERE email_cliente = ? AND senha = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(rs.getInt("id_cliente"), rs.getString("nome_cliente"),
                        rs.getString("email_cliente"), rs.getString("cpf"), rs.getString("senha"), rs.getBoolean("ativo"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public Cliente getByEmail(String email) {
        Cliente cliente = null;
        String query = "SELECT * FROM cliente WHERE email_cliente = ? AND ativo = TRUE";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(rs.getInt("id_cliente"), rs.getString("nome_cliente"),
                        rs.getString("email_cliente"), rs.getString("cpf"), rs.getString("senha"), rs.getBoolean("ativo"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cliente;
    }

    public Cliente getClienteById(int id) {
        Cliente cliente = null;
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nome_cliente"),
                        rs.getString("email_cliente"),
                        rs.getString("cpf"),
                        rs.getString("senha"),
                        rs.getBoolean("ativo"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cliente;
    }

    public void updateCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET nome_cliente = ?, email_cliente = ?, cpf = ?, senha = ? WHERE id_cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getSenha());
            stmt.setInt(5, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void desativarCliente(int clienteId) {
        String sql = "UPDATE cliente SET ativo = FALSE WHERE id_cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    

    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nome_cliente"),
                        rs.getString("email_cliente"),
                        rs.getString("cpf"),
                        rs.getString("senha"),
                        rs.getBoolean("ativo"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
}
