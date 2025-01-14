package app;

import static spark.Spark.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import model.*;
import service.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class Main {
    private static ClienteService clienteService = new ClienteService();
    private static AcademiaService academiaService = new AcademiaService();
    private static FavoritoService favoritoService = new FavoritoService();
    private static AvaliacaoService avaliacaoService = new AvaliacaoService();
    private static PagamentoService pagamentoService = new PagamentoService();

    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(4567);

        staticFiles.externalLocation("src/main/resources");

        before((request, response) -> {
            String userType = request.session().attribute("userType");
            String path = request.pathInfo();
            Integer academyId = request.session().attribute("userId");
            Integer clienteId = request.session().attribute("userId");

            // Redirecionar para a página de login se o usuário tentar acessar o painel da
            // academia e não for uma academia
            if (path.equals("/painel-acad") && !"academia".equals(userType)) {
                response.redirect("/login-acad");
                halt(); // Interrompe a execução da requisição
            }

            // Verifica se o cliente está tentando acessar alguma rota sem estar logado
            if (path.equals("/painel-cliente") && clienteId == null) {
                response.redirect("/login-cliente");
                halt();
            }
        });

        // formularios
        get("/cad-acad", (request, response) -> renderHTMLForm("src/main/resources/pages/cad-acad.html"));
        get("/cad-cliente", (request, response) -> renderHTMLForm("src/main/resources/pages/cad-cliente.html"));
        get("/login-acad", (request, response) -> renderHTMLForm("src/main/resources/pages/login-acad.html"));
        get("/login-cliente", (request, response) -> renderHTMLForm("src/main/resources/pages/login-cliente.html"));
        get("/edit-acad", (request, response) -> renderHTMLForm("src/main/resources/pages/edit-acad.html"));
        get("/edit-cliente", (request, response) -> renderHTMLForm("src/main/resources/pages/edit-cliente.html"));

        // rotas de páginas
        get("/", (request, response) -> renderHTMLForm("index.html"));
        get("/painel-acad", (request, response) -> renderHTMLForm("src/main/resources/pages/painel-acad.html"));
        get("/painel-cliente", (request, response) -> renderHTMLForm("src/main/resources/pages/painel-cliente.html"));
        get("/lista-acad", (request, response) -> renderHTMLForm("src/main/resources/pages/lista-acad.html"));
        get("/detalhes", (request, response) -> renderHTMLForm("src/main/resources/pages/detalhes.html"));
        get("/favs", (request, response) -> renderHTMLForm("src/main/resources/pages/favs.html"));
        get("/destaque", (request, response) -> renderHTMLForm("src/main/resources/pages/destaque.html"));
        get("/historico", (request, response) -> renderHTMLForm("src/main/resources/pages/historico.html"));
        get("/relatorio", (request, response) -> renderHTMLForm("src/main/resources/pages/relatorio.html"));

        // rotas faceapi
        get("/models/:filename", (request, response) -> {
            String filename = request.params(":filename");
            String path = "src/main/resources/FaceAPI/models/" + filename;
        
            if (Files.exists(Paths.get(path))) {
                response.type("application/json");
                return new String(Files.readAllBytes(Paths.get(path)));
            } else {
                response.status(404);
                return "Arquivo não encontrado.";
            }
        });        

        post("/cad-acad", (request, response) -> {
            Object result = academiaService.add(request, response);
            if (response.status() == 201) {
                response.redirect("/login-acad");
                halt();
            }
            return result;
        });

        post("/cad-cliente", (request, response) -> {
            Object result = clienteService.add(request, response);
            if (response.status() == 201) {
                response.redirect("/login-cliente");
                halt();
            }
            return result;
        });

        post("/login-acad", (request, response) -> {
            Object result = academiaService.login(request, response);
            if (response.status() == 200) {
                response.redirect("/painel-acad");
                halt();
            }
            return result;
        });

        post("/login-cliente", (request, response) -> {
            Object result = clienteService.login(request, response);
            if (response.status() == 200) {
                response.redirect("/lista-acad");
                halt();
            }
            return result;
        });

        get("/logout", (request, response) -> {
            request.session().invalidate();
            response.redirect("/");
            return null;
        });

        get("/api/academias", academiaService::getAllAcademias);

        get("/api/academias/:id", (request, response) -> {
            String id = request.params(":id");
            return academiaService.getAcademiaById(id);
        });

        get("/api/clientes", clienteService::getAllClientes);

        get("/api/clientes/:id", (request, response) -> {
            String id = request.params(":id");
            return clienteService.getClienteById(id);
        });

        get("/api/cliente-logado", (request, response) -> clienteService.getClienteLogado(request, response));
        put("/api/atualizar-cliente", (request, response) -> clienteService.atualizarCliente(request, response));
        delete("/api/excluir-cliente", (request, response) -> clienteService.desativarCliente(request, response));

        get("/api/academia-logado", (request, response) -> academiaService.getAcademiaLogada(request, response));
        put("/api/atualizar-academia", (request, response) -> academiaService.atualizarAcademia(request, response));
        delete("/api/excluir-academia", (request, response) -> academiaService.excluirAcademia(request, response));

        post("/api/favoritar", (request, response) -> {
            Integer clienteId = request.session().attribute("userId");
            Integer academiaId = new Gson().fromJson(request.body(), JsonObject.class).get("idAcademia").getAsInt();

            if (favoritoService.favoritarAcademia(clienteId, academiaId)) {
                response.status(200);
                return "Academia favoritada com sucesso!";
            } else {
                response.status(500);
                return "Erro ao favoritar academia.";
            }
        });

        post("/api/desfavoritar", (request, response) -> {
            Integer clienteId = request.session().attribute("userId");
            Integer academiaId = new Gson().fromJson(request.body(), JsonObject.class).get("idAcademia").getAsInt();

            if (favoritoService.desfavoritarAcademia(clienteId, academiaId)) {
                response.status(200);
                return "Academia desfavoritada com sucesso!";
            } else {
                response.status(500);
                return "Erro ao desfavoritar academia.";
            }
        });

        get("/api/favoritos", (request, response) -> {
            Integer clienteId = request.session().attribute("userId");
            List<Academia> academiasFavoritadas = favoritoService.getAcademiasFavoritadas(clienteId);
            return new Gson().toJson(academiasFavoritadas);
        });

        post("/pagamento", (request, response) -> {
            return academiaService.processarPagamento(request, response);
        });

        post("/api/avaliacao", (request, response) -> {
            response.type("application/json");
            Avaliacao avaliacao = gson.fromJson(request.body(), Avaliacao.class);
            boolean sucesso = avaliacaoService.adicionarAvaliacao(avaliacao);
            if (sucesso) {
                response.status(201);
                return gson.toJson("Avaliação adicionada com sucesso!");
            } else {
                response.status(500);
                return gson.toJson("Erro ao adicionar avaliação.");
            }
        });

        // Rota para buscar todas as avaliações de uma academia
        get("/api/academias/:id/avaliacoes", (request, response) -> {
            response.type("application/json");
            int idAcademia = Integer.parseInt(request.params("id"));
            return gson.toJson(avaliacaoService.getAvaliacoesPorAcademia(idAcademia));
        });

        post("/api/comprar", (request, response) -> {
            response.type("application/json");
            Pagamento pagamento = gson.fromJson(request.body(), Pagamento.class);

            String idAcademia = String.valueOf(pagamento.getAcademia().getIdAcademia());

            String academiaJson = (String) academiaService.getAcademiaById(idAcademia);

            if (academiaJson.contains("error")) {
                response.status(400);
                return academiaJson; // Retorna o erro
            }

            boolean sucesso = pagamentoService.adicionarPagamento(pagamento);
            if (sucesso) {
                response.status(201);
                return gson.toJson("Pagamento bem sucedido!");
            } else {
                response.status(500);
                return gson.toJson("Erro ao pagar.");
            }
        });

        get("/api/hist", (request, response) -> {
            Integer clienteId = request.session().attribute("userId");
            List<Academia> academiasPagas = pagamentoService.getAcademiasPagas(clienteId);
            return new Gson().toJson(academiasPagas);
        });

        get("/api/controle", (request, response) -> {
            Integer clienteId = request.session().attribute("userId");
            List<Cliente> clientesPagantes = pagamentoService.getClientesPagantes(clienteId);
            return new Gson().toJson(clientesPagantes);
        });

        get("/api/academia-logado/preco-final", (request, response) -> {
            Integer idAcademia = request.session().attribute("userId");
            if (idAcademia == null) {
                response.status(401);
                return "Usuário não logado como academia.";
            }
            double precoFinal = pagamentoService.somaPrecoFinal(idAcademia);
            response.type("application/json");
            return new Gson().toJson(precoFinal);
        });

    }

    private static String renderHTMLForm(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao carregar o formulário.";
        }
    }
}