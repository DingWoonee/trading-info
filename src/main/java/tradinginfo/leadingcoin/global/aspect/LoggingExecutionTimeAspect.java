package tradinginfo.leadingcoin.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingExecutionTimeAspect {

    @Around("@annotation(tradinginfo.leadingcoin.global.aspect.LogExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        LogExecutionTime timed = signature.getMethod().getAnnotation(LogExecutionTime.class);

        String name = timed.value().isEmpty()
                ? signature.getDeclaringType().getSimpleName() + "." + signature.getName()
                : timed.value();

        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("[ExecutionTime] {} executed in {} ms", name, duration);
        }
    }
}
