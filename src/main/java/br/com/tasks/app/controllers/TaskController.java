package br.com.tasks.app.controllers;

import br.com.tasks.app.models.Tarefa;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {
    
    @GetMapping("/")
    public String index(Model model) {
        List<Tarefa> tarefas = 
                Tarefa.listar(jdbc);
        model.addAttribute("tarefas",
                tarefas);
        return "index";
    }
    
    @Autowired
    private JdbcTemplate jdbc;
    
    @PostMapping("/criar")
    public String criar(String nome,
            int prioridade, String descricao) {
        Tarefa t = new Tarefa(nome,
                prioridade, descricao);
        t.salvar(jdbc);
        return "redirect:/";
    }
    
    @PostMapping("/atualizar")
    public String atualizar(int id, String nome,
            int prioridade, String descricao) {
        Tarefa t = Tarefa.buscar(jdbc, id);
        t.setDescricao(descricao);
        t.setPrioridade(prioridade);
        t.setNome(nome);
        t.atualizar(jdbc);
        return "redirect:/";
    }
    
    @GetMapping("/remover")
    public String remover(int id) {
        Tarefa.excluir(jdbc, id);
        return "redirect:/";
    }
    
    @GetMapping("/editar")
    public String editar(int id, Model model) {
        Tarefa t = Tarefa.buscar(jdbc, id);
        List<Tarefa> tarefas = 
                Tarefa.listar(jdbc);
        model.addAttribute("tarefas",
                tarefas);
        model.addAttribute("t", t);
        return "index";
    }
    
}
