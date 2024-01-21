package com.bsupply.productdashboard.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public @interface ClearDbBeforeTestMethod {

    String CLEANUP_SCRIPT = "classpath:/sql/cleanup_script.sql";
    String INITIALISE_SCRIPT = "classpath:/sql/init_data.sql";

    @AliasFor(annotation = Sql.class, attribute = "scripts")
    String[] db() default {
            CLEANUP_SCRIPT,
            INITIALISE_SCRIPT
    };


}
