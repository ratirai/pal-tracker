package io.pivotal.pal.tracker;

import org.springframework.stereotype.Repository;

import java.util.List;


public interface TimeEntryRepository {
    TimeEntry create(TimeEntry any);

    TimeEntry find(long timeEntryId);

    TimeEntry update(long eq, TimeEntry any);

    void delete(long timeEntryId);

    List<TimeEntry> list();
}
