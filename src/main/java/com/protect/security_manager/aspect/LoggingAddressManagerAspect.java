package com.protect.security_manager.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAddressManagerAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAddressManagerAspect.class);

    // Avant l'exécution de toute méthode du service ManageAddressService
    @Before("execution(* com.protect.security_manager.service.ManageAddressService.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("Méthode appelée: " + joinPoint.getSignature().getName() + " avec les arguments " + java.util.Arrays.toString(joinPoint.getArgs()));
    }

    // Après le retour d'une méthode avec succès
    @AfterReturning(pointcut = "execution(* com.protect.security_manager.service.ManageAddressService.*(..))", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        logger.info("Méthode exécutée: " + joinPoint.getSignature().getName() + " a retourné: " + result);
    }

    // En cas d'exception
    @AfterThrowing(pointcut = "execution(* com.protect.security_manager.service.ManageAddressService.*(..))", throwing = "exception")
    public void logAfterException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Méthode " + joinPoint.getSignature().getName() + " a lancé une exception: " + exception.getMessage(), exception);
    }
}
