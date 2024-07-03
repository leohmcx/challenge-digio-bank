package com.bank.monitoring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.bank.monitoring.Observable.OperationType.NONE;
import static com.bank.monitoring.ObservableConstants.SERVICE_DEFAULT;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Observable {

    String operation();
    String service() default SERVICE_DEFAULT;
    OperationType type() default NONE;

    enum OperationType {
        MESSAGING, REST, SOAP, NONE
    }
}
