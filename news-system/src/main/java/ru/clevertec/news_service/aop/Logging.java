package ru.clevertec.news_service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class Logging {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerClassMethods() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceClassMethods() {
    }

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repositoryClassMethods() {
    }

    @Pointcut("controllerClassMethods() || serviceClassMethods() || repositoryClassMethods()")
    public void restControllerServiceMethods() {
    }

    @Pointcut("@annotation(ru.clevertec.news_service.aop.annotation.Logging)")
    public void loggingAnnotatedMethods() {}

    @Around("controllerClassMethods()")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {

        ResponseEntity<?> result = (ResponseEntity<?>) joinPoint.proceed();
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();

        log.info("Signature: {}. Args list: {}. Return: {}. Status code: {}",
                signature, args, result.getBody(), result.getStatusCode());
        return result;
    }

    @AfterThrowing(value = "restControllerServiceMethods()", throwing = "ex")
    public void logControllerException(JoinPoint joinPoint, Exception ex) {

        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String exceptionTitle = ex.getClass().getName();
        String exceptionMessage = ex.getMessage();

        log.warn("Signature: {}. Args list: {}. Exception title: {}. Exception message: {}",
                signature, args, exceptionTitle, exceptionMessage);
    }

    @Around("loggingAnnotatedMethods()")
    public Object logStateChangedMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        Object result = joinPoint.proceed();

        log.info("Signature: {}. Args: {}. Result: {} {}", signature, args, methodName, result);
        return result;
    }
}
