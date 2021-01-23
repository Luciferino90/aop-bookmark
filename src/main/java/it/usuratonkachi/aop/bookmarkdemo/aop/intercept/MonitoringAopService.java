package it.usuratonkachi.aop.bookmarkdemo.aop.intercept;

import it.usuratonkachi.aop.bookmarkdemo.utils.BookmarkUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Slf4j
@Aspect
@Service
@Order(2)
@RequiredArgsConstructor
public class MonitoringAopService {

    private final ChronoUnit unit = ChronoUnit.MILLIS;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;


    @Pointcut("@annotation(it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable)")
    public void monitoringMethod() {}

    @Around("monitoringMethod()")
    public Object monitorMethodTime(ProceedingJoinPoint joinPoint) throws Throwable {
        ZonedDateTime start = ZonedDateTime.now();
        String bookmarkName = BookmarkUtils.getBookmarkName(joinPoint);
        log.info(bookmarkName + ": Started at " + start.format(dateTimeFormatter));
        Object ret = joinPoint.proceed();
        log.info(bookmarkName + ": Ended at " + start.format(dateTimeFormatter) + " in " + unit.between(start, ZonedDateTime.now()) + " ms");
        return ret;
    }

}
