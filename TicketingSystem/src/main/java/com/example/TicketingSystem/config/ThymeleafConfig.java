//package com.example.TicketingSystem.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.spring6.SpringTemplateEngine;
//import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
//
//@Configuration
//public class ThymeleafConfig {
//
//    @Bean
//    public TemplateEngine templateEngine() {
//        SpringTemplateEngine engine = new SpringTemplateEngine();
//        engine.setTemplateResolver(templateResolver());
//        return engine;
//    }
//
//    @Bean
//    public ClassLoaderTemplateResolver templateResolver() {
//        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
//        resolver.setPrefix("templates/");   // Looks inside src/main/resources/templates/
//        resolver.setSuffix(".html");        // Only HTML files
//        resolver.setTemplateMode("HTML");   // Template mode set to HTML
//        resolver.setCharacterEncoding("UTF-8");
//        return resolver;
//    }
//}
