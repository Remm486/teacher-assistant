package com.assistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket 配置类
 *
 * 注册 ServerEndpointExporter Bean，
 * 使 Spring 容器能扫描并激活 @ServerEndpoint 注解的端点类。
 * 如果不配置此 Bean，WebSocket 端点将无法正常工作。
 */
@Configuration
public class WebSocketConfig {

    /**
     * 注册 WebSocket 端点扫描器
     * 该 Bean 会自动扫描所有带有 @ServerEndpoint 注解的类并注册到内嵌 Tomcat
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
