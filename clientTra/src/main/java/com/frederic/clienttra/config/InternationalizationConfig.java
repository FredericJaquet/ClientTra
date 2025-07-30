package com.frederic.clienttra.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuration class for internationalization and validation message sources.
 * <p>
 * This class sets up the {@link MessageSource} used for message resolution
 * in the application (e.g., validation messages) and configures the
 * {@link LocalValidatorFactoryBean} to use this message source.
 */
@Configuration
public class InternationalizationConfig {

    /**
     * Defines the message source bean used for internationalization.
     *
     * @return a {@link MessageSource} with UTF-8 encoding and messages loaded
     *         from "classpath:messages"
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    /**
     * Configures the validator factory bean to use the defined message source
     * for validation messages.
     *
     * @param messageSource the message source to be used for validation messages
     * @return a {@link LocalValidatorFactoryBean} configured with the message source
     */
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource);
        return factoryBean;
    }

}
