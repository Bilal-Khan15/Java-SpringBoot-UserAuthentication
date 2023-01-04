package com.callsign.userauthentication.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "com.callsign.userauthentication")
@EnableAspectJAutoProxy
public class BeanConfig {
}
