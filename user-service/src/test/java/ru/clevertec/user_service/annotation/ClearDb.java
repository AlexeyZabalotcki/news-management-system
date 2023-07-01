package ru.clevertec.user_service.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import ru.clevertec.user_service.util.ClearDatabaseExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ClearDatabaseExtension.class)
@TestPropertySource(properties = {
        "liquibase.change-log=classpath:/db/changelog/db.changelog-master.yaml",
        "liquibase.should-run=true",
        "liquibase.drop-first=true"})
public @interface ClearDb {
}
