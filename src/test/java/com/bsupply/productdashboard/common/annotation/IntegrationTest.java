package com.bsupply.productdashboard.common.annotation;

import com.bsupply.productdashboard.ProductDashboardApplication;
import com.bsupply.productdashboard.common.container.PostgresqlTestContainer;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProductDashboardApplication.class)
@ContextConfiguration(initializers = {PostgresqlTestContainer.Initializer.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Commit
@ClearDbBeforeTestMethod
public @interface IntegrationTest {
}
