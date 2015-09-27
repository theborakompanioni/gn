package com.github.theborakompanioni.gn;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by void on 20.09.15.
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public Jdk8Module jdk8Module() {
        return new Jdk8Module();
    }
}
