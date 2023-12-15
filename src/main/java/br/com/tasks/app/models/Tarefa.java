package br.com.tasks.app.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.jdbc.core.JdbcTemplate;

public class Tarefa {
    
    private int id;
    private String nome;
    private int prioridade;
    private String descricao;
    private int feito;

    public Tarefa(String nome, int prioridade, String descricao) {
        this.nome = nome;
        this.prioridade = prioridade;
        this.descricao = descricao;
        this.feito = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public int getFeito() {
        return feito;
    }
    
    public void salvar(JdbcTemplate jdbc) {
        jdbc.update("INSERT INTO tarefas "
        + "(nome, prioridade, descricao) "
        + "VALUES (?, ?, ?);", (ps) -> {
            ps.setString(1, nome);
            ps.setInt(2, prioridade);
            ps.setString(3, descricao);
        });
    }
    
    public void atualizar(JdbcTemplate jdbc) {
        jdbc.update("UPDATE tarefas SET "
        + "nome = ?, prioridade = ?,"
        + "descricao = ? WHERE id = ?;", (ps) -> {
            ps.setString(1, nome);
            ps.setInt(2, prioridade);
            ps.setString(3, descricao);
            ps.setInt(4, id);
        });
    }
    
    public static List<Tarefa>
        listar(JdbcTemplate jdbc) {
        List<Tarefa> tarefas = new ArrayList<>();
        jdbc.query("SELECT * FROM tarefas "
                + "ORDER BY prioridade;", (rs) -> {
                    do {
                        Tarefa t = new Tarefa(rs.getString("nome"),
                                rs.getInt("prioridade"),
                                rs.getString("descricao"));
                        t.feito = rs.getInt("feita");
                        t.id = rs.getInt("id");
                        tarefas.add(t);
                    } while(rs.next());
                });
        return tarefas;
    }
        
    public static void excluir(JdbcTemplate jdbc,
            int id) {
        jdbc.update("DELETE FROM tarefas"
        + " WHERE id = ?", (ps) -> {
            ps.setInt(1, id);
        });
    }
    
    public static Tarefa buscar(JdbcTemplate jdbc,
            int id) {
        AtomicReference<Tarefa> tarefa =
                new AtomicReference<>();
        jdbc.query("SELECT * FROM tarefas "
        + "WHERE id = ?", (ps) -> {
            ps.setInt(1, id);
        }, (rs) -> {
            Tarefa t = new Tarefa(
                    rs.getString("nome"),
                    rs.getInt("prioridade"),
                    rs.getString("descricao"));
            t.feito = rs.getInt("feita");
            t.id = rs.getInt("id");
            tarefa.set(t);
        });
        return tarefa.get();
    }
    
}
