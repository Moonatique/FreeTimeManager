package fr.unice.polytech.freetimedatabase;

import android.provider.BaseColumns;

/**
 * Created by Hakim on 09/06/2014.
 */
public final class FreeTimeDbContract {

    //empty constructor to prevent accidentally instantiating the contract class
    public FreeTimeDbContract() {}

    /* Inner class that defines the unoccupiedtime table contents */
    public static abstract class EmptySlots implements BaseColumns {
        public static final String TABLE_NAME = "empty_slots";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
    }

    public static abstract class Tasks implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ESTIMATION = "estimation";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_USER_PRIORITY = "priority";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_ONE_TIME = "is_one_time";
        public static final String COLUMN_RECURRING = "is_recurring";
        public static final String COLUMN_LONGTERM = "is_longterm";

    }

    public static abstract class FtEvents implements BaseColumns {
        public static final String TABLE_NAME = "ft_events";
        public static final String COLUMN_EVENT_ID = "event_id";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_COMPLETED = "completed";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TYPE = "type";
    }

    public static abstract class TaskFtEvents implements  BaseColumns {
        public static final String TABLE_NAME = "task_ftevents";
        public static final String COLUMN_FTEVENT_ID = "ftevent_id";
        public static final String COLUMN_TASK_ID = "task_id";
    }

    public static abstract class FreeTimeBlocks implements BaseColumns {
        public static final String TABLE_NAME = "freetimeblock";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_EVENT_ID = "event_id";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
    }
}
