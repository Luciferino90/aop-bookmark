package it.usuratonkachi.aop.bookmarkdemo.runner;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Monitorable;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step1_Filtering;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step2_Modifying;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step3_FetchResultAndSave;
import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class StepService {

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step1_Filtering.class, missingBookmarkFailure = false)
    public Mono<WrapperContext> step1_filtering(WrapperContext wrapperContext){
        return Mono.just(wrapperContext)
                .map(wrapper -> {
                    log.info("PING from step1_filtering");
                    return wrapperContext;
                });
    }

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step2_Modifying.class)
    public Mono<WrapperContext> step2_modifying(WrapperContext wrapperContext){
        return Mono.just(wrapperContext)
                .map(wrapper -> {
                    log.info("PING from step2_modifying");
                    return wrapperContext;
                });
    }

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step3_FetchResultAndSave.class)
    public Mono<WrapperContext> step3_fetchResultAndSave(WrapperContext wrapperContext){
        return Mono.just(wrapperContext)
                .map(wrapper -> {
                    log.info("PING from step3_fetchResultAndSave");
                    return wrapperContext;
                });
    }

}
