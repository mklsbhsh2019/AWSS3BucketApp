package com.s3example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class DateGenerator {

    public List<String> generateDates(String startDateStr, String endDateStr) {
        try {
            List<String> datesRange = new ArrayList<>();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate;
            try {
                startDate = dateFormat.parse(startDateStr);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            Date endDate;
            try {
                endDate = dateFormat.parse(endDateStr);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            // Create a calendar instance for the start date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            // Loop through the dates from the start date to the end date
            while (calendar.getTime().getTime() <= endDate.getTime()) {
                Date currentDate = calendar.getTime();
                datesRange.add(dateFormat.format(currentDate));
                calendar.add(Calendar.DATE, 1); // Add one day to the calendar
            }
            return makeObjectName(datesRange);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public List<String> makeObjectName(List<String> inputDates) {
        try {
            List<String> objectNames = new ArrayList<>();
            String extension = ".json";
            for (String str : inputDates
            ) {
                String key = str.replaceAll("-", "");
                objectNames.add(str + "/" + key + extension);
            }
            return objectNames;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}