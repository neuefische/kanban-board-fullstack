package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KanbanRepositoryTest {

    @Test
    void shouldReturnTasks() {
        var kanbanRepository = new KanbanRepository();

        kanbanRepository.save(new Task("Task 1", "Desc 1", TaskStatus.OPEN));

        Assertions.assertThat(kanbanRepository.findAll()).hasSize(1);
    }

    @Test
    void shouldDeleteTask() {
        // Given
        var kanbanRepository = new KanbanRepository();
        Task task = new Task("Task 1", "Desc 1", TaskStatus.OPEN);
        kanbanRepository.save(task);

        // When
        kanbanRepository.deleteById(task.getId());

        // Then
        Assertions.assertThat(kanbanRepository.findAll()).isEmpty();
    }

}