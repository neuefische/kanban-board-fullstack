package de.neuefische.muc.kanban;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KanbanRepository extends MongoRepository<Task, String> {

    List<Task> findAllByUserId(String userId);

    Optional<Task> deleteByIdAndUserId(String id, String userId);

    Optional<Task> findByIdAndUserId(String id, String userId);

}
