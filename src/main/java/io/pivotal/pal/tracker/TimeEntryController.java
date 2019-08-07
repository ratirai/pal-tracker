package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository=timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntryNew=timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        return new ResponseEntity(timeEntryNew,HttpStatus.CREATED);
    }

    @GetMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {

        TimeEntry entry= timeEntryRepository.find(timeEntryId);
        if(entry!=null){
            actionCounter.increment();
        return  new ResponseEntity(entry, HttpStatus.OK);}
        else{
            return  new ResponseEntity(entry, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{timeEntryId}")
    public ResponseEntity delete(@PathVariable  long timeEntryId)
    {
        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntry) {
        TimeEntry timeEntryUpdated=timeEntryRepository.update(timeEntryId,timeEntry);
        if(timeEntryUpdated==null)
        { actionCounter.increment();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity(timeEntryUpdated,HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        List<TimeEntry> timeEntryList=timeEntryRepository.list();
        return new ResponseEntity(timeEntryList,HttpStatus.OK);
    }
}
