package com.re.taskmanager.controller;



import com.re.taskmanager.model.entity.Todo;
import com.re.taskmanager.repository.TodoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class TodoController {

    @Autowired
    private final TodoRepository todoRepository;


    @GetMapping("/")
    public String listTodos(Model model) {
        model.addAttribute("todos", todoRepository.findAll());
        model.addAttribute("todo", new Todo());
        return "todo-list";
    }


    @PostMapping("/add")
    public String addTodo(@Valid @ModelAttribute("todo") Todo todo,
                          BindingResult result,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("todos", todoRepository.findAll());
            model.addAttribute("hasError", true);
            return "todo-list";
        }

        todoRepository.save(todo);
        return "redirect:/";
    }

}
