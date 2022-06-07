package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
