package com.example.demo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class KanbanRepository {

    private List<Task> tasks = new ArrayList<>();

    public List<Task> findAll() {
        return tasks;
    }

    public void save(Task task) {
        int index = tasks.indexOf(task);
        if (index < 0) {
            tasks.add(task);
        } else {
            tasks.remove(index);
            tasks.add(index, task);
        }
    }

    public void deleteById(String id) {
        tasks.removeIf(task -> Objects.equals(task.getId(), id));
    }

    public Optional<Task> findById(String id) {
        return tasks.stream()
                .filter(task -> Objects.equals(task.getId(), id))
                .findFirst();
    }
}
