package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class KanbanServiceTest {

    @Test
    void shouldReturnTasks() {
        // Given
        List<Task> tasks = List.of(
                new Task("Einkaufen", "Viel!!!", TaskStatus.OPEN),
                new Task("Vorcoden", "Spring Boot", TaskStatus.IN_PROGRESS)
        );

        KanbanRepository kanbanRepository = Mockito.mock(KanbanRepository.class);
        Mockito.when(kanbanRepository.findAll()).thenReturn(tasks);

        KanbanService kanbanService = new KanbanService(kanbanRepository);

        // When
        List<Task> actual = kanbanService.findAll();

        // Then
        Assertions.assertThat(actual).hasSize(2);
    }

    @Test
    void shouldCreateNewTask() {
        KanbanRepository kanbanRepository = Mockito.mock(KanbanRepository.class);
        KanbanService kanbanService = new KanbanService(kanbanRepository);

        Task newTask = new Task("Neu", "Beschreibung", TaskStatus.OPEN);
        kanbanService.createTask(newTask);

        Mockito.verify(kanbanRepository).save(newTask);
    }

}
