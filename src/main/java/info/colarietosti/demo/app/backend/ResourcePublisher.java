package info.colarietosti.demo.app.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcePublisher implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations("file:/opt/docs/")
                .addResourceLocations("file:/home/cola/sandbox/CVHost/Data/")
                .addResourceLocations("file:/C:/Sandbox/CVHost/Data/");
    }

}
