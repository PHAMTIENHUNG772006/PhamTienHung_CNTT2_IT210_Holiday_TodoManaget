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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @GetMapping("/edit/{id}")
    public String editTodo(@PathVariable Long id, Model model) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid todo Id:" + id));
        model.addAttribute("todo", todo);
        model.addAttribute("todos", todoRepository.findAll());
        model.addAttribute("showForm", true);
        return "todo-list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        todoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công task ID: " + id);
        return "redirect:/";
    }
}
