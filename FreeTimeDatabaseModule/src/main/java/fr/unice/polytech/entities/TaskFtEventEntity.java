package fr.unice.polytech.entities;

/**
 * Created by Hakim on 20/06/2014.
 */
public class TaskFtEventEntity {
    private long id;
    private long ftEventId;
    private long taskId;

    public TaskFtEventEntity(long id, long ftEventId, long taskId) {
        this.id = id;
        this.ftEventId = ftEventId;
        this.taskId = taskId;
    }
}
