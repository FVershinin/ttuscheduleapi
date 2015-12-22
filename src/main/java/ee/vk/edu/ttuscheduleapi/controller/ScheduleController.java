package ee.vk.edu.ttuscheduleapi.controller;

import com.google.common.collect.Maps;
import net.fortuna.ical4j.data.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ee.vk.edu.ttuscheduleapi.client.TTUSchedule;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v2/schedules")
public class ScheduleController {

    @Autowired
    private TTUSchedule ttuSchedule;

    @RequestMapping
    public ResponseEntity<List<String>> findAll(){
        return new ResponseEntity<>(ttuSchedule.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> find(@PathVariable(value="id") String group) throws IOException, ParserException, ParseException {
        Map<String, Object> eventMap = Maps.newLinkedHashMap();
        eventMap.put("name", group.toUpperCase());
        eventMap.put("events", ttuSchedule.find(group));
        return new ResponseEntity<>(eventMap, HttpStatus.OK);
    }
}
