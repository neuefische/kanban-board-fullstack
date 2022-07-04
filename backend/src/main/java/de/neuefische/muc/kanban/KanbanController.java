package de.neuefische.muc.kanban;

import de.neuefische.muc.kanban.user.KanbanUser;
import de.neuefische.muc.kanban.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/kanban")
@CrossOrigin
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;
    private final UserService userService;

    @GetMapping
    public List<Task> getTasks(Principal principal) {
        KanbanUser user = userService.findByUsername(principal.getName()).orElseThrow();
        return kanbanService.findAll(user.getId());
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable String taskId, Principal principal) {
        KanbanUser user = userService.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.of(kanbanService.findById(taskId, user.getId()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@RequestBody Task task, Principal principal) {
        KanbanUser user = userService.findByUsername(principal.getName()).orElseThrow();
        kanbanService.createTask(task, user.getId());
    }

    @PutMapping
    public void editTask(@RequestBody Task task, Principal principal) {
        KanbanUser user = userService.findByUsername(principal.getName()).orElseThrow();
        kanbanService.editTask(task, user.getId());
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId, Principal principal) {
        try {
            KanbanUser user = userService.findByUsername(principal.getName()).orElseThrow();
            kanbanService.deleteTask(taskId, user.getId());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/next")
    public void pushForward(@RequestBody Task task, Principal principal) {
        KanbanUser user = userService.findByUsername(principal.getName()).orElseThrow();
        kanbanService.promoteTask(task, user.getId());
    }

    @PutMapping("/prev")
    public void pullBackward(@RequestBody Task task, Principal principal) {
        KanbanUser user = userService.findByUsername(principal.getName()).orElseThrow();
        kanbanService.demoteTask(task, user.getId());
    }

}
