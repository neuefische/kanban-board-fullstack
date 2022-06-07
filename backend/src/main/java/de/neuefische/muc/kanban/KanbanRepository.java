package de.neuefische.muc.kanban;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class KanbanRepository {

    private final List<Task> tasks = new ArrayList<>();

    public List<Task> findAll() {
        return tasks;
    }

    public void save(Task task) {
        findById(task.getId())
                .ifPresentOrElse(
                        savedTask -> {
                            int index = tasks.indexOf(savedTask);
                            tasks.set(index, task);
                        },
                        () -> tasks.add(task)
                );
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
