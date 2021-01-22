package it.usuratonkachi.aop.bookmarkdemo.runner;

import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Bookmarkable;
import it.usuratonkachi.aop.bookmarkdemo.aop.annotation.Monitorable;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step1_Filtering;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step2_Modifying;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.Step3_FetchResultAndSave;
import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import org.springframework.stereotype.Service;

@Service
public class StepService {

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step1_Filtering.class, missingBookmarkFailure = false)
    public WrapperContext step1_filtering(WrapperContext wrapperContext){
        System.out.println("PING from step1_filtering");
        return wrapperContext;
    }

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step2_Modifying.class)
    public WrapperContext step2_modifying(WrapperContext wrapperContext){
        System.out.println("PING from step2_modifying");
        return wrapperContext;
    }

    @Monitorable
    @Bookmarkable(bookmarkJavaType = Step3_FetchResultAndSave.class)
    public WrapperContext step3_fetchResultAndSave(WrapperContext wrapperContext){
        System.out.println("PING from step3_fetchResultAndSave");
        return wrapperContext;
    }

}
