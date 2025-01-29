package com.turkcell.mini_e_commere_hw2.core.configuration;

import an.awesome.pipelinr.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelineConfiguration {
    @Bean
    Pipeline pipeline (ObjectProvider<Command.Handler> commandHandlers,
                       ObjectProvider<Notification.Handler> notificationHandlers,
                       ObjectProvider<Command.Middleware> middlewares) {
        return new Pipelinr()
                .with(commandHandlers::stream)
                .with(notificationHandlers::stream)
                .with(middlewares::orderedStream);
    }
}
