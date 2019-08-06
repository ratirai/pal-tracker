package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    public long counter=1L;
    public HashMap<Long ,TimeEntry> repo=new HashMap<Long, TimeEntry>();

    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry entry=new TimeEntry(counter,timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getDate(),timeEntry.getHours());
        repo.put(counter,entry);
        ++counter;
        return entry;
    }

    public TimeEntry find(long id) {
        if(!repo.containsKey(id)){
            return null;
        }
        return repo.get(id);
    }

    public List<TimeEntry> list() {
        List<TimeEntry> list = new ArrayList<TimeEntry>(repo.values());
        return  list;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {

        TimeEntry entry=new TimeEntry();
        TimeEntry entry1=find(id);
        if(entry1 !=null) {
            entry.setId(id);
            entry.setDate(timeEntry.getDate());
            entry.setHours(timeEntry.getHours());
            entry.setProjectId(timeEntry.getProjectId());
            entry.setUserId(timeEntry.getUserId());
            repo.replace(id, entry);
            return entry;
        }
        else{
            return null;
        }
    }

    public void delete(long id) {
        repo.remove(id);
    }
}
