package de.neuefische.muc.kanban;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

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
    void shouldReturnOneTask() {
        // Given
        var task = new Task("Einkaufen", "Viel!!!", TaskStatus.OPEN);

        KanbanRepository kanbanRepository = Mockito.mock(KanbanRepository.class);
        Mockito.when(kanbanRepository.findById(task.getId())).thenReturn(Optional.of(task));

        KanbanService kanbanService = new KanbanService(kanbanRepository);

        // When
        Optional<Task> actual = kanbanService.findById(task.getId());

        // Then
        Assertions.assertThat(actual).contains(task);
    }

    @Test
    void shouldCreateNewTask() {
        KanbanRepository kanbanRepository = Mockito.mock(KanbanRepository.class);
        KanbanService kanbanService = new KanbanService(kanbanRepository);

        Task newTask = new Task("Neu", "Beschreibung", TaskStatus.OPEN);
        kanbanService.createTask(newTask);

        Mockito.verify(kanbanRepository).save(newTask);
    }

    @Test
    void shouldEditExistingTask() {
        KanbanRepository kanbanRepository = Mockito.mock(KanbanRepository.class);
        KanbanService kanbanService = new KanbanService(kanbanRepository);

        Task taskToEdit = new Task("Neu", "Beschreibung", TaskStatus.OPEN);
        kanbanService.editTask(taskToEdit);

        Mockito.verify(kanbanRepository).save(taskToEdit);
    }

    @Test
    void shouldPromoteTask() {
        Task taskToEdit = new Task("Wäsche waschen", "Buhhh!", TaskStatus.OPEN);
        Task taskToSave = new Task(taskToEdit.getId(), "Wäsche waschen", "Buhhh!", TaskStatus.IN_PROGRESS);

        KanbanRepository kanbanRepository = Mockito.mock(KanbanRepository.class);
        Mockito.when(kanbanRepository.findById(taskToEdit.getId())).thenReturn(Optional.of(taskToEdit));

        KanbanService kanbanService = new KanbanService(kanbanRepository);
        kanbanService.promoteTask(taskToEdit);

        Mockito.verify(kanbanRepository).save(taskToSave);
    }

    @Test
    void shouldDemoteTask() {
        Task taskToEdit = new Task("Wäsche waschen", "Buhhh!", TaskStatus.DONE);
        Task taskToSave = new Task(taskToEdit.getId(), "Wäsche waschen", "Buhhh!", TaskStatus.IN_PROGRESS);

        KanbanRepository kanbanRepository = Mockito.mock(KanbanRepository.class);
        Mockito.when(kanbanRepository.findById(taskToEdit.getId())).thenReturn(Optional.of(taskToEdit));

        KanbanService kanbanService = new KanbanService(kanbanRepository);
        kanbanService.demoteTask(taskToEdit);

        Mockito.verify(kanbanRepository).save(taskToSave);
    }

    @Test
    void shouldDeleteTask() {
        KanbanRepository kanbanRepository = Mockito.mock(KanbanRepository.class);
        KanbanService kanbanService = new KanbanService(kanbanRepository);

        kanbanService.deleteTask("4711");

        Mockito.verify(kanbanRepository).deleteById("4711");
    }

}
