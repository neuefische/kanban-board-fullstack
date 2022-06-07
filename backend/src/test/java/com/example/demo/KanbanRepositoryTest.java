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

}