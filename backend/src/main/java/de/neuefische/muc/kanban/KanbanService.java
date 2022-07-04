package de.neuefische.muc.kanban;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KanbanService {

    private final KanbanRepository kanbanRepository;

    public List<Task> findAll(String userId) {
        return kanbanRepository.findAllByUserId(userId);
    }

    public void createTask(Task task, String userId) {
        task.setUserId(userId);
        kanbanRepository.save(task);
    }

    public void deleteTask(String taskId, String userId) {
        kanbanRepository.deleteByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new IllegalStateException("du darfst das nicht!!!!!!!!!"));
    }

    public void promoteTask(Task task, String userId) {
        kanbanRepository.findByIdAndUserId(task.getId(), userId)
                .map(taskFromDB -> taskFromDB.setStatus(taskFromDB.getStatus().next()))
                .ifPresent(kanbanRepository::save);
    }

    public void demoteTask(Task task, String userId) {
        kanbanRepository.findByIdAndUserId(task.getId(), userId)
                .map(taskFromDB -> taskFromDB.setStatus(taskFromDB.getStatus().prev()))
                .ifPresent(kanbanRepository::save);
    }

    public Optional<Task> findById(String id, String userId) {
        return kanbanRepository.findByIdAndUserId(id, userId);
    }

    public void editTask(Task task, String userId) {
        kanbanRepository.findByIdAndUserId(task.getId(), userId)
                .map(kanbanRepository::save)
                .orElseThrow(() -> new IllegalStateException("forbidden"));
    }
}
