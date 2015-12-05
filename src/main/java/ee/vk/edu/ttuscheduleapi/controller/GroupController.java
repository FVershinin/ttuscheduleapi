package ee.vk.edu.ttuscheduleapi.controller;

import com.google.common.collect.Maps;
import ee.vk.edu.ttuscheduleapi.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ee.vk.edu.ttuscheduleapi.client.TTUSchedule;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    @Autowired
    private TTUSchedule ttuSchedule;

    @RequestMapping
    public ResponseEntity<Map<String, Object>> getAllGroups(){
        Map<String, Object> groupMap = Maps.newLinkedHashMap();
        groupMap.put("groups", ttuSchedule.getAllGroups());
        return new ResponseEntity<>(groupMap, HttpStatus.OK);
    }
}
