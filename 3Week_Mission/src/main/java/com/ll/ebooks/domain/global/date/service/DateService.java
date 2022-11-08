package com.ll.ebooks.domain.global.date.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Service
public class DateService {
    //각 월의 마지막 날짜 구하기
    public String getEndDayOfMonth(String yearMonth) {

        int date[] = Stream.of(yearMonth.split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();

        YearMonth thisYearMonth = YearMonth.of(date[0], date[1]);
        LocalDate endMonth = thisYearMonth.atEndOfMonth();

        return String.valueOf(endMonth);
    }
    //string to localDateTime
    public LocalDateTime mapToLocalDateTime(String date) {
        return mapToLocalDateTime("yyyy-MM-dd HH:mm:ss.SSSSSS", date);
    }

    public LocalDateTime mapToLocalDateTime(String pattern, String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

}
