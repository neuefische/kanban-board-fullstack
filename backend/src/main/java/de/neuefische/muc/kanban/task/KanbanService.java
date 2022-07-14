package de.neuefische.muc.kanban.task;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KanbanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KanbanService.class);

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
                .map(Task::promote)
                .ifPresent(kanbanRepository::save);
    }

    public void demoteTask(Task task, String userId) {
        kanbanRepository.findByIdAndUserId(task.getId(), userId)
                .map(Task::demote)
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

    public void createTasks(InputStream inputStream, String id) {

        try (Reader reader = new InputStreamReader(inputStream)) {
            CsvToBean<CsvTask> build = new CsvToBeanBuilder<CsvTask>(reader)
                    .withType(CsvTask.class)
                    .build();

            List<Task> tasks = build.parse().stream()
                    .map(csvTask -> new Task(csvTask.getTask(), csvTask.getDescription(), TaskStatus.valueOf(csvTask.getStatus())))
                    .map(task -> task.setUserId(id))
                    .toList();
            kanbanRepository.saveAll(tasks);
        } catch (IOException e) {
            LOGGER.warn("csv could not be imported", e);
            throw new RuntimeException(e);
        }

    }
}
