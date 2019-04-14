package com.silviomoser.demo.config;

import com.captcha.botdetect.web.servlet.SimpleCaptchaServlet;
import com.silviomoser.demo.view.ExcelViewResolver;
import com.silviomoser.demo.view.PdfViewResolver;
import com.vaadin.spring.annotation.EnableVaadin;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableVaadin
@Slf4j
public class WebApplicationConfiguration implements WebMvcConfigurer {

    @Autowired
    ImageServiceConfiguration imageServiceConfiguration;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/notFound").setViewName("forward:/index.html");
    }


    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notFound"));
        };
    }

    @Bean
    public ViewResolver excelViewResolver() {
        return new ExcelViewResolver();
    }

    @Bean
    public ViewResolver pdfViewResolver() {
        return new PdfViewResolver();
    }


    @Bean
    ServletRegistrationBean simpleCaptchaServletRegistration () {
        ServletRegistrationBean srb = new ServletRegistrationBean();
        srb.setServlet(new SimpleCaptchaServlet());
        srb.addUrlMappings("/botdetectcaptcha");
        return srb;
    }

    @Bean
    public ServletContextInitializer initializer() {
        return servletContext -> servletContext.setInitParameter("BDC_configFileLocation", "/resources/botdetect.xml");
    }

    @Bean
    public MinioClient getMinioClient() {

        try {
            return new MinioClient(imageServiceConfiguration.getEndpoint(), imageServiceConfiguration.getAccessKey(), imageServiceConfiguration.getSecretKey());

        } catch (InvalidEndpointException | InvalidPortException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
