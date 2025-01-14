package service;

import dao.AcademiaDAO;
import model.Academia;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AcademiaService {
    private AcademiaDAO academiaDAO;
    private Gson gson = new Gson();

    public AcademiaService() {
        academiaDAO = new AcademiaDAO();
    }

    public Object add(Request request, Response response) {
        String nome = request.queryParams("nome_academia");
        String localizacao = request.queryParams("localizacao");
        String email = request.queryParams("email");
        String senha = request.queryParams("senha");
        String cnpj = request.queryParams("cnpj");
        String precoAulaStr = request.queryParams("preco_aula");
        String fotoPerfil = request.queryParams("foto_perfil");
    
        if (nome == null || nome.isEmpty() || localizacao == null || localizacao.isEmpty() || email == null
                || email.isEmpty() || senha == null || senha.isEmpty() || cnpj == null || cnpj.isEmpty()
                || precoAulaStr == null || precoAulaStr.isEmpty()) {
            response.status(400);
            return "Erro: Todos os campos devem ser preenchidos.";
        }
    
        Float precoAula = Float.parseFloat(precoAulaStr); // Convert to Float
    
        // Criptografar a senha antes de armazenar
        String hashedSenha = BCrypt.hashpw(senha, BCrypt.gensalt());
    
        Academia academia = new Academia(-1, nome, localizacao, false, email, hashedSenha, cnpj, precoAula, fotoPerfil);
        academiaDAO.insert(academia); // destaque é false por padrão
        response.status(201);
        return "Academia cadastrada com sucesso!";
    }    

    public Object login(Request request, Response response) {
        String email = request.queryParams("email");
        String senha = request.queryParams("senha");
    
        Academia academia = academiaDAO.getByEmail(email); // Método para obter a academia pelo email
    
        if (academia != null && BCrypt.checkpw(senha, academia.getSenha())) {
            request.session().attribute("userType", "academia");
            request.session().attribute("userId", academia.getIdAcademia());
            response.status(200);
            return "Login bem-sucedido!";
        } else {
            response.status(401);
            return "Erro: Email ou senha incorretos.";
        }
    }     

    public Object getAllAcademias(Request request, Response response) {
        List<Academia> academias = academiaDAO.getAllAcademias();
        Collections.sort(academias, Comparator.comparing(Academia::getDestaque).reversed());
        response.type("application/json");
        return gson.toJson(academias);
    }

    public Object getAcademiaById(String id) {

        try {
            int academiaId = Integer.parseInt(id);
            Academia academia = academiaDAO.getAcademiaById(academiaId);
            if (academia != null) {
                // Convert Academia object to a Map and return as JSON
                return gson.toJson(Map.of(
                        "nomeAcademia", academia.getNomeAcademia(),
                        "localizacao", academia.getLocalizacao(),
                        "precoAula", academia.getPrecoAula(),
                        "fotoPerfil", academia.getFotoPerfil()));
            } else {
                return gson.toJson(Map.of("error", "Academia não encontrada"));
            }
        } catch (NumberFormatException e) {
            return gson.toJson(Map.of("error", "Erro: ID inválido."));
        }
    }

    public Object getAcademiaLogada(Request request, Response response) {
        Integer academiaId = request.session().attribute("userId");

        if (academiaId != null) {
            Academia academia = academiaDAO.getAcademiaById(academiaId);
            if (academia != null) {
                response.status(200);
                return gson.toJson(academia);
            } else {
                response.status(404);
                return "Academia não encontrado";
            }
        } else {
            response.status(401);
            return "Usuário não logado";
        }
    }

    public Object atualizarAcademia(Request request, Response response) {
        Integer academiaId = request.session().attribute("userId");
    
        if (academiaId != null) {
            Academia academia = academiaDAO.getAcademiaById(academiaId);
    
            if (academia != null) {
                // Atualiza os dados da Academia com os novos valores recebidos do formulário
                Map<String, String> updates = gson.fromJson(request.body(), Map.class);
                academia.setNomeAcademia(updates.get("nomeAcademia"));
                academia.setLocalizacao(updates.get("localizacao"));
                academia.setEmail(updates.get("email"));
                
                // Verificar se a senha foi alterada
                if (updates.containsKey("senha")) {
                    String senhaCriptografada = BCrypt.hashpw(updates.get("senha"), BCrypt.gensalt());
                    academia.setSenha(senhaCriptografada);
                }
                
                academia.setCnpj(updates.get("cnpj"));
                academia.setPrecoAula(Float.parseFloat(updates.get("precoAula")));
                academia.setFotoPerfil(updates.get("fotoPerfil"));
    
                academiaDAO.updateAcademia(academia);
                response.status(200);
                return "Academia atualizada com sucesso!";
            } else {
                response.status(404);
                return "Academia não encontrada";
            }
        } else {
            response.status(401);
            return "Usuário não logado";
        }
    }    

    public Object excluirAcademia(Request request, Response response) {
        Integer academiaId = request.session().attribute("userId");

        if (academiaId != null) {
            Academia Academia = academiaDAO.getAcademiaById(academiaId);

            if (Academia != null) {
                academiaDAO.deleteAcademia(academiaId);
                request.session().removeAttribute("userId"); // Remove a sessão do Academia
                response.status(200);
                return "Academia excluído com sucesso";
            } else {
                response.status(404);
                return "Academia não encontrado";
            }
        } else {
            response.status(401);
            return "Usuário não logado";
        }
    }

    public Object processarPagamento(Request request, Response response) {
        Integer academiaId = request.session().attribute("userId");

        if (academiaId != null) {
            // Aqui você pode simular o pagamento
            // Atualiza o atributo 'destaque' da academia para true
            Academia academia = academiaDAO.getAcademiaById(academiaId);
            if (academia != null) {
                academia.setDestaque(true); // Definir destaque como true
                academiaDAO.updateAcademia(academia); // Atualiza a academia no banco
                response.status(200);
                return "Pagamento processado com sucesso! Academia destacada.";
            } else {
                response.status(404);
                return "Academia não encontrada";
            }
        } else {
            response.status(401);
            return "Usuário não logado";
        }
    }

}