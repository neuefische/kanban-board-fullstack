package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KanbanService {

    private final KanbanRepository kanbanRepository;

    public List<Task> findAll() {
        return kanbanRepository.findAll();
    }

    public void createTask(Task task) {
        kanbanRepository.save(task);
    }

    public void deleteTask(String taskId) {
        kanbanRepository.deleteById(taskId);
    }

    public void promoteTask(Task task) {
        kanbanRepository.findById(task.getId())
                .map(taskFromDB -> taskFromDB.setStatus(taskFromDB.getStatus().next()))
                .ifPresent(kanbanRepository::save);
    }

    public void demoteTask(Task task) {
        kanbanRepository.findById(task.getId())
                .map(taskFromDB -> taskFromDB.setStatus(taskFromDB.getStatus().prev()))
                .ifPresent(kanbanRepository::save);
    }

    public Optional<Task> findById(String id) {
        return kanbanRepository.findById(id);
    }

    public void editTask(Task task) {
        kanbanRepository.save(task);
    }
}
