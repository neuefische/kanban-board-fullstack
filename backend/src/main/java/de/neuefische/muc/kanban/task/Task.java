package de.neuefische.muc.kanban.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    private String id;
    private String task;
    private String description;
    private TaskStatus status;
    private String userId;

    public Task(String task, String description, TaskStatus status) {
        this.task = task;
        this.description = description;
        this.status = status;
    }

    public Task setStatus(TaskStatus status) {
        this.status = status;
        return this;
    }
}
