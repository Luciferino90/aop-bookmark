package it.usuratonkachi.aop.bookmarkdemo.runner.steps;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Monitorable;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step1_Filtering;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class Step1_Service {

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step1_Filtering.class)
    public Mono<Envelope> run(Envelope envelope){
        return Mono.just(envelope)
                .map(wrapper -> {
                    log.info("PING from step1_filtering");
                    return envelope;
                });
    }

}
