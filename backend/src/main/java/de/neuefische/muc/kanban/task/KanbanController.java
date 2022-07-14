package de.neuefische.muc.kanban.task;

import de.neuefische.muc.kanban.user.User;
import de.neuefische.muc.kanban.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        return kanbanService.findAll(user.getId());
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable String taskId, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.of(kanbanService.findById(taskId, user.getId()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@RequestBody Task task, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        kanbanService.createTask(task, user.getId());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createTasks(@RequestParam("csv") MultipartFile file, Principal principal) throws Exception {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        try {
            kanbanService.createTasks(file.getInputStream(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public void editTask(@RequestBody Task task, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        kanbanService.editTask(task, user.getId());
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId, Principal principal) {
        try {
            User user = userService.findByUsername(principal.getName()).orElseThrow();
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
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        kanbanService.promoteTask(task, user.getId());
    }

    @PutMapping("/prev")
    public void pullBackward(@RequestBody Task task, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        kanbanService.demoteTask(task, user.getId());
    }

}
