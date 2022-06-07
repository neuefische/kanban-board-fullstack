package de.neuefische.muc.kanban;

public enum TaskStatus {

    OPEN,
    IN_PROGRESS,
    DONE;

    public TaskStatus next() {
        if (this == OPEN) {
            return IN_PROGRESS;
        } else if (this == IN_PROGRESS) {
            return DONE;
        }
        throw new RuntimeException("unknown status");
    }

    public TaskStatus prev() {
        if (this == DONE) {
            return IN_PROGRESS;
        } else if (this == IN_PROGRESS) {
            return OPEN;
        }
        throw new RuntimeException("unknown status");
    }
}
