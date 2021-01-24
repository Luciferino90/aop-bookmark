package it.usuratonkachi.aop.bookmarkdemo.runner;

import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import it.usuratonkachi.aop.bookmarkdemo.runner.steps.Step1_Service;
import it.usuratonkachi.aop.bookmarkdemo.runner.steps.Step2_Service;
import it.usuratonkachi.aop.bookmarkdemo.runner.steps.Step3_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

/**
 * Stupid service which will call a couple of Steps
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RunnerService {

    @Value("${bookmark.mode}")
    private String bookmarkMode;

    private final Step1_Service step1_service;
    private final Step2_Service step2_service;
    private final Step3_Service step3_service;

    public Mono<Envelope> runCleverBoy(String address) {
        ChronoUnit chronoUnit = ChronoUnit.SECONDS;
        ZonedDateTime start = ZonedDateTime.now();
        int min = 0;
        int max = 1000;
        return Flux.fromStream(IntStream.range(min, max).boxed())
                .map(i -> Envelope.factory(address))
                .flatMap(step1_service::run)
                .flatMap(step2_service::run)
                .flatMap(step3_service::run)
                .last()
                .doOnNext(e -> log.info(String.format("FINITO! Modalità %s %s messaggi in %s secondi", bookmarkMode, max, chronoUnit.between(start, ZonedDateTime.now()))));
    }

}
