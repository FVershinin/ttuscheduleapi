package rest.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TTUSchedule {
    private static final String GROUPS_URL = "https://ois.ttu.ee/portal/page?_pageid=35,435155&_dad=portal&_schema=PORTAL&i=2&e=-1&e_sem=161&a=1&b=%1$d&c=-1&d=-1&k=&q=neto&g=";
    private static final String CALENDAR_URL = "https://ois.ttu.ee/pls/portal/tunniplaan.PRC_EXPORT_DATA?p_page=view_plaan&pn=i&pv=2&pn=e_sem&pv=161&pn=e&pv=-1&pn=b&pv=1&pn=g&pv=%1$d&pn=is_oppejoud&pv=false&pn=q&pv=1";

    private List<Calendar> calendars;
    private Map<String, Integer> groupMap;

    public TTUSchedule() throws IOException {
        System.setProperty("https.protocols", "TLSv1,SSLv3,SSLv2Hello");
        calendars = Lists.newLinkedList();
        groupMap = getAllGroupsId();
    }

    public void sync(List<String> groups) throws IOException, ParserException {
        for (String group : groups){
            URL url = new URL(String.format(CALENDAR_URL, groupMap.get(group)));
            CalendarBuilder calendarBuilder = new CalendarBuilder();
            calendars.add(calendarBuilder.build(url.openConnection().getInputStream()));
        }
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    private Map<String, Integer> getAllGroupsId() throws IOException {
        Map<String, Integer> map = Maps.newLinkedHashMap();
        Pattern pattern = Pattern.compile("g=(\\w+)");
        for (int i = 1; i <= 2; i++) {
            for (Element span : Jsoup.connect(String.format(GROUPS_URL, i)).timeout(10000).get().select("span").select("span:has(a)")) {
                Matcher matcher = pattern.matcher(span.attr("onclick"));
                if (matcher.find())
                    map.put(span.select("a").html(), Integer.valueOf(matcher.group(1)));
            }
        }
        return map;
    }
}