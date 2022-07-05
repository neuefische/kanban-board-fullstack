package de.neuefische.muc.kanban;

import de.neuefische.muc.kanban.task.KanbanRepository;
import de.neuefische.muc.kanban.task.KanbanService;
import de.neuefische.muc.kanban.task.Task;
import de.neuefische.muc.kanban.task.TaskStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

public class KanbanServiceTest {

    @Test
    void shouldReturnTasks() {
        // Given
        List<Task> tasks = List.of(
                new Task("Einkaufen", "Viel!!!", TaskStatus.OPEN),
                new Task("Vorcoden", "Spring Boot", TaskStatus.IN_PROGRESS)
        );

        KanbanRepository kanbanRepository = mock(KanbanRepository.class);
        when(kanbanRepository.findAllByUserId("testUserId")).thenReturn(tasks);

        KanbanService kanbanService = new KanbanService(kanbanRepository);

        // When
        List<Task> actual = kanbanService.findAll("testUserId");

        // Then
        Assertions.assertThat(actual).hasSize(2);
    }

    @Test
    void shouldReturnOneTask() {
        // Given
        var task = new Task("Einkaufen", "Viel!!!", TaskStatus.OPEN);

        KanbanRepository kanbanRepository = mock(KanbanRepository.class);
        when(kanbanRepository.findByIdAndUserId(task.getId(), "testUserId")).thenReturn(Optional.of(task));

        KanbanService kanbanService = new KanbanService(kanbanRepository);

        // When
        Optional<Task> actual = kanbanService.findById(task.getId(), "testUserId");

        // Then
        Assertions.assertThat(actual).contains(task);
    }

    @Test
    void shouldCreateNewTask() {
        KanbanRepository kanbanRepository = mock(KanbanRepository.class);
        KanbanService kanbanService = new KanbanService(kanbanRepository);

        Task newTask = new Task("Neu", "Beschreibung", TaskStatus.OPEN);
        kanbanService.createTask(newTask, "testUserId");

        Task taskToBeSaved = new Task(null, "Neu", "Beschreibung", TaskStatus.OPEN, "testUserId");
        verify(kanbanRepository).save(taskToBeSaved);
    }

    @Test
    void shouldEditExistingTask() {
        Task taskToEdit = new Task("the-id", "Neu", "Beschreibung", TaskStatus.OPEN, "testUserId");
        Task savedTask = new Task("the-id", "Neu", "Beschreibung", TaskStatus.OPEN, "testUserId");
        KanbanRepository kanbanRepository = mock(KanbanRepository.class);
        when(kanbanRepository.findByIdAndUserId("the-id", "testUserId")).thenReturn(Optional.of(savedTask));
        when(kanbanRepository.save(taskToEdit)).thenReturn(taskToEdit);
        KanbanService kanbanService = new KanbanService(kanbanRepository);

        assertThatNoException()
                .isThrownBy(() -> kanbanService.editTask(taskToEdit, "testUserId"));
    }

    @Test
    void shouldNotEditExistingTask() {
        Task taskToEdit = new Task("the-id", "Neu", "Beschreibung", TaskStatus.OPEN, "testUserId");
        KanbanRepository kanbanRepository = mock(KanbanRepository.class);
        when(kanbanRepository.findByIdAndUserId("the-id", "testUserId")).thenReturn(Optional.empty());

        KanbanService kanbanService = new KanbanService(kanbanRepository);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> kanbanService.editTask(taskToEdit, "testUserId"));
    }

    @Test
    void shouldPromoteTask() {
        Task taskToEdit = new Task("Wäsche waschen", "Buhhh!", TaskStatus.OPEN);
        taskToEdit.setUserId("testUserId");
        Task taskToSave = new Task(taskToEdit.getId(), "Wäsche waschen", "Buhhh!", TaskStatus.IN_PROGRESS, "testUserId");

        KanbanRepository kanbanRepository = mock(KanbanRepository.class);
        when(kanbanRepository.findByIdAndUserId(taskToEdit.getId(), "testUserId")).thenReturn(Optional.of(taskToEdit));

        KanbanService kanbanService = new KanbanService(kanbanRepository);
        kanbanService.promoteTask(taskToEdit, "testUserId");

        verify(kanbanRepository).save(taskToSave);
    }

    @Test
    void shouldDemoteTask() {
        Task taskToEdit = new Task("Wäsche waschen", "Buhhh!", TaskStatus.DONE);
        taskToEdit.setUserId("testUserId");
        Task taskToSave = new Task(taskToEdit.getId(), "Wäsche waschen", "Buhhh!", TaskStatus.IN_PROGRESS, "testUserId");

        KanbanRepository kanbanRepository = mock(KanbanRepository.class);
        when(kanbanRepository.findByIdAndUserId(taskToEdit.getId(), "testUserId")).thenReturn(Optional.of(taskToEdit));

        KanbanService kanbanService = new KanbanService(kanbanRepository);
        kanbanService.demoteTask(taskToEdit, "testUserId");

        verify(kanbanRepository).save(taskToSave);
    }

    @Test
    void shouldDeleteTask() {
        KanbanRepository kanbanRepository = mock(KanbanRepository.class);
        when(kanbanRepository.deleteByIdAndUserId("4711", "testUserId")).thenReturn(Optional.of(new Task()));
        KanbanService kanbanService = new KanbanService(kanbanRepository);

        assertThatNoException()
                .isThrownBy(() -> kanbanService.deleteTask("4711", "testUserId"));
    }

    @Test
    void shouldNotDeleteTask() {
        KanbanRepository kanbanRepository = mock(KanbanRepository.class);
        when(kanbanRepository.deleteByIdAndUserId("4711", "testUserId")).thenReturn(Optional.empty());
        KanbanService kanbanService = new KanbanService(kanbanRepository);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> kanbanService.deleteTask("4711", "testUserId"));
    }

}
