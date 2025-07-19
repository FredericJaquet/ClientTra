package com.frederic.clienttra.dto.create;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringJUnitConfig(classes = com.frederic.clienttra.config.InternationalizationConfig.class)
public abstract class AbstractValidationTest {

    protected Validator validator;

    @Autowired
    private MessageSource messageSource;

    @BeforeEach
    void setUpValidatorWithMessageSource() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource); // aquí conectamos la traducción
        factoryBean.afterPropertiesSet();
        validator = factoryBean.getValidator();
    }
}
