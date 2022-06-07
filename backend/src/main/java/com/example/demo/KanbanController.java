package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kanban")
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;

    @GetMapping
    public List<Task> getTasks() {
        return kanbanService.findAll();
    }

    @PostMapping
    public void createTask(@RequestBody Task task) {
        kanbanService.createTask(task);
    }

}
