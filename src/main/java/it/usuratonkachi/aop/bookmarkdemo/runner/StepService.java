package it.usuratonkachi.aop.bookmarkdemo.runner;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Monitorable;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step1_Filtering;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step2_Date;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step3_Object;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class StepService {

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step1_Filtering.class, missingBookmarkFailure = false)
    public Mono<Envelope> step1_filtering(Envelope wrapperContext){
        return Mono.just(wrapperContext)
                .map(wrapper -> {
                    log.info("PING from step1_filtering");
                    return wrapperContext;
                });
    }

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step2_Date.class)
    public Mono<Envelope> step2_modifying(Envelope wrapperContext){
        return Mono.just(wrapperContext)
                .map(wrapper -> {
                    log.info("PING from step2_modifying");
                    return wrapperContext;
                });
    }

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step3_Object.class)
    public Mono<Envelope> step3_fetchResultAndSave(Envelope wrapperContext){
        return Mono.just(wrapperContext)
                .map(wrapper -> {
                    log.info("PING from step3_fetchResultAndSave");
                    return wrapperContext;
                });
    }

}
