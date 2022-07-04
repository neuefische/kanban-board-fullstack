package de.neuefische.muc.kanban;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanbanRepository extends MongoRepository<Task, String> {
}