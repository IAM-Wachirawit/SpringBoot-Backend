package com.nitendo.backend.schedule;

import com.nitendo.backend.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserSchedule {

    private final UserService userService;

    public UserSchedule(UserService userService) {
        this.userService = userService;
    }

    // Schedule Note
    // 1 => second
    // 2 => minute
    // 3 => hour
    // 4 => day
    // 5 => month
    // 6 => year

    /** Function Evert minute **/
    @Scheduled(cron = "0 * * * * *")
    public void testEveryMinute() {     // run ทุก 1 นาที คือ ทุกวินาทีที่เป็น 0 จะเท่ากับ 1 นาที
        log.info("Hello, What's up?");
    }

    /** Function Every day at 00:00 (UTC Time) **/
    @Scheduled(cron = "0 0 0 * * *")
    public void testEveryMidNight() {

    }

    /** Function Every day at 15:35 (Thailand Time)**/
    @Scheduled(cron = "0 35 15 * * *", zone = "Asia/Bangkok")
    public void testEverydayNineAM() {
        log.info("Hell Test Function Every day");
    }

}
