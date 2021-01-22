package it.usuratonkachi.aop.bookmarkdemo.runner;

import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Stupid service which will call a couple of Steps
 */
@Service
@RequiredArgsConstructor
public class RunnerService {

    private final StepService stepService;

    public Mono<WrapperContext> runCleverBoy(String address) {
        return Mono.just(WrapperContext.factory(address))
                .map(stepService::step1_filtering)
                .map(stepService::step2_modifying)
                .map(stepService::step3_fetchResultAndSave);
    }

}
