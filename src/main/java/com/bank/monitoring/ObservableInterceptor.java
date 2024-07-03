package com.bank.monitoring;

import com.bank.exception.AbstractErrorException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Executor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import static com.bank.monitoring.ObservableConstants.ERROR_CAUSE_TAG;
import static com.bank.monitoring.ObservableConstants.ERROR_TAG;
import static com.bank.monitoring.ObservableConstants.OPERATION_TAG;
import static com.bank.monitoring.ObservableConstants.OPERATION_TYPE_TAG;
import static com.bank.monitoring.ObservableConstants.RESULT_NOK_VALUE;
import static com.bank.monitoring.ObservableConstants.RESULT_OK_VALUE;
import static com.bank.monitoring.ObservableConstants.RESULT_TAG;
import static com.bank.monitoring.ObservableConstants.SERVICE_REQUEST;
import static com.bank.monitoring.ObservableConstants.SERVICE_TAG;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
class ObservableInterceptor {

    private final Executor executor;
    private final MeterRegistry meterRegistry;

    @Around("execution (@com.bank.Observable * *.*(..))")
    public Object observe(final ProceedingJoinPoint pjp) throws Throwable {
        log.debug("Intercepting observable method: {}", pjp);
        final var init = System.currentTimeMillis();
        try {
            final var result = pjp.proceed();
            final var duration = System.currentTimeMillis() - init;
            executor.execute(() -> register(pjp, duration, null));

            return result;
        } catch (final Throwable throwable) {
            final long duration = System.currentTimeMillis() - init;
            executor.execute(() -> register(pjp, duration, throwable));

            throw throwable;
        }
    }

    private void register(final ProceedingJoinPoint pjp, final Long durationInMillis, final Throwable thr) {
        try {
            log.debug("Registering metrics: {}", pjp);
            final var method = ((MethodSignature) pjp.getSignature()).getMethod();
            final var observable = method.getAnnotation(Observable.class);

            final Collection<Tag> tags = new ArrayList<>(resultAndErrorTags(thr));
            tags.add(Tag.of(OPERATION_TAG, observable.operation()));
            tags.add(Tag.of(OPERATION_TYPE_TAG, observable.type().toString().toLowerCase()));
            tags.add(Tag.of(SERVICE_TAG, observable.service()));

            recordMetric(durationInMillis, tags);
        } catch (final Throwable throwable) { // NOSONAR
            handleException(pjp, throwable);
        }
    }

    void recordMetric(final Long durationInMillis, final Collection<Tag> tags) {
        Timer.builder(SERVICE_REQUEST).tags(tags).register(meterRegistry).record(durationInMillis, MILLISECONDS);
    }

    void handleException(final ProceedingJoinPoint pjp, final Throwable throwable) {
        log.warn("Error while collecting metrics of an observable method: {}", pjp, throwable);
    }

    private static Collection<Tag> resultAndErrorTags(final Throwable thr) {
        return Arrays.asList(
                Tag.of(RESULT_TAG, ofNullable(thr).map(t -> RESULT_NOK_VALUE).orElse(RESULT_OK_VALUE)),
                Tag.of(ERROR_TAG, ofNullable(thr).map(t -> t.getClass().getSimpleName()).orElse(EMPTY)),
                Tag.of(ERROR_CAUSE_TAG, ofNullable(thr).filter(AbstractErrorException.class::isInstance)
                        .map(t -> (AbstractErrorException) t).map(aee -> aee.getErrorType().toString()).orElse(EMPTY))
        );
    }
}
