package service;

import java.util.Map;
import dao.ClienteDAO;
import model.Cliente;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

public class ClienteService {
    private ClienteDAO clienteDAO;
    private Gson gson = new Gson();

    public ClienteService() {
        clienteDAO = new ClienteDAO();
    }

    public Object add(Request request, Response response) {
        String nome = request.queryParams("nome");
        String email = request.queryParams("email");
        String cpf = request.queryParams("cpf");
        String senha = request.queryParams("senha");

        if (nome == null || nome.isEmpty() || email == null || email.isEmpty() || cpf == null || cpf.isEmpty()
                || senha == null || senha.isEmpty()) {
            response.status(400);
            return "Erro: Todos os campos devem ser preenchidos.";
        }

        String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());

        Cliente cliente = new Cliente(-1, nome, email, cpf, senhaCriptografada, true);
        clienteDAO.insert(cliente);
        response.status(201);
        return "Cliente cadastrado com sucesso!";
    }

    public Object login(Request request, Response response) {
        String email = request.queryParams("email");
        String senha = request.queryParams("senha");

        if (email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
            response.status(400);
            return "Erro: Email e senha são obrigatórios.";
        }

        Cliente cliente = clienteDAO.getByEmail(email);

        if (cliente != null && BCrypt.checkpw(senha, cliente.getSenha())) {
            request.session().attribute("userId", cliente.getId());
            request.session().attribute("userType", "cliente");
            response.status(200);
            return "Login realizado com sucesso!";
        } else {
            response.status(401);
            return "Erro: Email ou senha incorretos.";
        }
    }

    public Object atualizarCliente(Request request, Response response) {
        Integer clienteId = request.session().attribute("userId");

        if (clienteId != null) {
            Cliente cliente = clienteDAO.getClienteById(clienteId);

            if (cliente != null) {
                Map<String, String> updates = gson.fromJson(request.body(), Map.class);
                cliente.setNome(updates.get("nome"));
                cliente.setEmail(updates.get("email"));
                cliente.setCpf(updates.get("cpf"));

                if (updates.containsKey("senha")) {
                    String senhaCriptografada = BCrypt.hashpw(updates.get("senha"), BCrypt.gensalt());
                    cliente.setSenha(senhaCriptografada);
                }

                clienteDAO.updateCliente(cliente);
                response.status(200);
                return "Cliente atualizado com sucesso!";
            } else {
                response.status(404);
                return "Cliente não encontrado";
            }
        } else {
            response.status(401);
            return "Usuário não logado";
        }
    }

    public Object getCliente(Request request, Response response) {
        String clienteIdStr = request.session().attribute("clienteId");

        if (clienteIdStr != null) {
            try {
                int clienteId = Integer.parseInt(clienteIdStr);

                Cliente cliente = clienteDAO.getClienteById(clienteId);

                if (cliente != null) {
                    response.status(200);
                    return cliente;
                } else {
                    response.status(404);
                    return "Cliente não encontrado";
                }
            } catch (NumberFormatException e) {
                response.status(400);
                return "ID do cliente inválido";
            }
        } else {
            response.status(401);
            return "Usuário não logado";
        }
    }

    public Object getClienteLogado(Request request, Response response) {
        Integer clienteId = request.session().attribute("userId");

        if (clienteId != null) {
            Cliente cliente = clienteDAO.getClienteById(clienteId);
            if (cliente != null) {
                response.status(200);
                return gson.toJson(cliente); // Retorna os dados do cliente como JSON
            } else {
                response.status(404);
                return "Cliente não encontrado";
            }
        } else {
            response.status(401);
            return "Usuário não logado";
        }
    }

    public Object desativarCliente(Request request, Response response) {
        Integer clienteId = request.session().attribute("userId");
    
        if (clienteId != null) {
            Cliente cliente = clienteDAO.getClienteById(clienteId);
    
            if (cliente != null) {
                clienteDAO.desativarCliente(clienteId);  // Alterado para desativar o cliente
                request.session().removeAttribute("userId"); // Remove a sessão do cliente
                response.status(200);
                return "Cliente desativado com sucesso";
            } else {
                response.status(404);
                return "Cliente não encontrado";
            }
        } else {
            response.status(401);
            return "Usuário não logado";
        }
    }    

    public Object getAllClientes(Request request, Response response) {
        return gson.toJson(clienteDAO.getAllClientes());
    }

    public Object getClienteById(String id) {
        try {
            int clienteId = Integer.parseInt(id);
            Cliente cliente = clienteDAO.getClienteById(clienteId);
            if (cliente != null) {
                // Convert cliente object to a Map and return as JSON
                return gson.toJson(Map.of(
                        "id", cliente.getId(),
                        "nome", cliente.getNome(),
                        "email", cliente.getEmail(),
                        "cpf", cliente.getCpf()));
            } else {
                return gson.toJson(Map.of("error", "cliente não encontrada"));
            }
        } catch (NumberFormatException e) {
            return gson.toJson(Map.of("error", "Erro: ID inválido."));
        }
    }
}
