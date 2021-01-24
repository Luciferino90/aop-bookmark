package it.usuratonkachi.aop.bookmarkdemo.runner.steps;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Monitorable;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step3_Object;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Slf4j
@Service
public class Step3_Service {

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step3_Object.class)
    public Mono<Envelope> run(Envelope envelope){
        return Mono.just(envelope)
                .map(wrapper -> {
                    log.info("PING from step3_fetchResultAndSave");
                    Step3_Object step3_object = (Step3_Object) envelope.getBookmark(Step3_Object.class.getName()).getData();
                    step3_object.setFailedObject(new HashMap<>());
                    return envelope;
                });
    }

}
