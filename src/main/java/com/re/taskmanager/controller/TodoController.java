package com.re.taskmanager.controller;

import com.re.taskmanager.model.entity.Todo;
import com.re.taskmanager.repository.TodoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class TodoController {

    private final TodoRepository todoRepository;

    @GetMapping("/")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String name,
                          HttpSession session,
                          Model model) {

        if (name == null || name.trim().isEmpty()) {
            model.addAttribute("message", "Tên không được để trống");
            return "login";
        }

        session.setAttribute("name", name);
        return "redirect:/todo";
    }

    @GetMapping("/todo")
    public String listTodos(Model model, HttpSession session) {

        String name = (String) session.getAttribute("name");


        if (name == null) {
            return "redirect:/";
        }

        model.addAttribute("name", name);
        model.addAttribute("todos", todoRepository.findAll());
        model.addAttribute("todo", new Todo());

        return "todo-list";
    }

    @PostMapping("/add")
    public String addTodo(@Valid @ModelAttribute("todo") Todo todo,
                          BindingResult result,
                          Model model,
                          HttpSession session) {

        String name = (String) session.getAttribute("name");

        if (name == null) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            model.addAttribute("name", name);
            model.addAttribute("todos", todoRepository.findAll());
            model.addAttribute("hasError", true);
            return "todo-list";
        }

        todoRepository.save(todo);


        return "redirect:/todo";
    }

    @GetMapping("/edit/{id}")
    public String editTodo(@PathVariable Long id,
                           Model model,
                           HttpSession session) {

        String name = (String) session.getAttribute("name");

        if (name == null) {
            return "redirect:/";
        }

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid todo Id:" + id));

        model.addAttribute("todo", todo);
        model.addAttribute("name", name);
        model.addAttribute("todos", todoRepository.findAll());
        model.addAttribute("showForm", true);

        return "todo-list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes,
                         HttpSession session) {

        String name = (String) session.getAttribute("name");

        if (name == null) {
            return "redirect:/";
        }

        todoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa thành công task ID: " + id);

        return "redirect:/todo";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}