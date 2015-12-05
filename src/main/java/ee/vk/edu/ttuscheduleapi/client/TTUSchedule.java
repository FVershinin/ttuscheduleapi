package ee.vk.edu.ttuscheduleapi.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ee.vk.edu.ttuscheduleapi.model.Group;
import ee.vk.edu.ttuscheduleapi.model.Event;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class TTUSchedule {
    private static final String GROUPS_URL = "https://ois.ttu.ee/portal/page?_pageid=35,435155&_dad=portal&_schema=PORTAL&i=2&e=-1&e_sem=161&a=1&b=%1$d&c=-1&d=-1&k=&q=neto&g=";
    private static final String CALENDAR_URL = "https://ois.ttu.ee/pls/portal/tunniplaan.PRC_EXPORT_DATA?p_page=view_plaan&pn=i&pv=2&pn=e_sem&pv=161&pn=e&pv=-1&pn=b&pv=%1$d&pn=g&pv=%2$d&pn=is_oppejoud&pv=false&pn=q&pv=1";

    private Map<String, Group> groupsMap;

    public TTUSchedule() throws IOException {
        System.setProperty("https.protocols", "TLSv1,SSLv3,SSLv2Hello");
        groupsMap = getGroupsMap();
    }

    public List<Event> getEvents(String group_name) throws IOException, ParserException, ParseException {
        List<Event> events = Lists.newLinkedList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("EET"));
        Group group = groupsMap.get(group_name.toUpperCase());
        URL url = new URL(String.format(CALENDAR_URL, group.getType(), group.getId()));
        CalendarBuilder calendarBuilder = new CalendarBuilder();
        ComponentList components = calendarBuilder.build(url.openConnection().getInputStream()).getComponents(Component.VEVENT);
        for (Object object : components) {
            Component component = (Component) object;
            Event event = new Event();
            event.setDateStart(dateFormat.parse(component.getProperty(Property.DTSTART).getValue()));
            event.setDateEnd(dateFormat.parse(component.getProperty(Property.DTEND).getValue()));
            event.setDescription(component.getProperty(Property.DESCRIPTION).getValue());
            event.setLocation(component.getProperty(Property.LOCATION).getValue());
            event.setSummary(component.getProperty(Property.SUMMARY).getValue());
            events.add(event);
        }
        return events;
    }

    public List<Group> getAllGroups() {
        return Lists.newLinkedList(groupsMap.values());
    }

    private Map<String, Group> getGroupsMap() throws IOException {
        Map<String, Group> map = Maps.newLinkedHashMap();
        Pattern pattern = Pattern.compile("g=(\\w+)");
        for (int i = 1; i <= 2; i++) {
            for (Element span : Jsoup.connect(String.format(GROUPS_URL, i)).timeout(15000).get().select("span").select("span:has(a)")) {
                Matcher matcher = pattern.matcher(span.attr("onclick"));
                if (matcher.find()) {
                    Group group = new Group(Long.valueOf(matcher.group(1)), span.select("a").html(), i);
                    map.put(group.getName(), group);
                }
            }
        }
        return map;
    }
}