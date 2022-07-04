package de.neuefische.muc.kanban;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kanban")
@CrossOrigin
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;

    @GetMapping
    public List<Task> getTasks() {
        return kanbanService.findAll();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable String taskId) {
        return ResponseEntity.of(kanbanService.findById(taskId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@RequestBody Task task) {
        kanbanService.createTask(task);
    }

    @PutMapping
    public void editTask(@RequestBody Task task) {
        kanbanService.editTask(task);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        try {
            kanbanService.deleteTask(taskId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/next")
    public void pushForward(@RequestBody Task task) {
        kanbanService.promoteTask(task);
    }

    @PutMapping("/prev")
    public void pullBackward(@RequestBody Task task) {
        kanbanService.demoteTask(task);
    }

}
