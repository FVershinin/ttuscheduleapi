package ee.vk.edu.ttuscheduleapi.controller;

import com.google.common.collect.Lists;
import ee.vk.edu.ttuscheduleapi.model.Subject;
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
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private TTUSchedule ttuSchedule;


    @RequestMapping
    public ResponseEntity<Map<String, List<Subject>>> Schedules(@RequestParam(value="groups", required=true) String[] group) throws IOException, ParserException, ParseException {
        return new ResponseEntity<>(ttuSchedule.getCalendars(Lists.newArrayList(group)), HttpStatus.OK);
    }
}
