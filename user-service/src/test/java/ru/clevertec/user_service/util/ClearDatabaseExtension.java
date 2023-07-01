package ru.clevertec.user_service.util;

import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class ClearDatabaseExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        SpringLiquibase liquibase = SpringExtension.getApplicationContext(context)
                .getBean(SpringLiquibase.class);
        liquibase.setDropFirst(true);
        liquibase.afterPropertiesSet();
    }
}
