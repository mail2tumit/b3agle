package xyz.tumit.b3agle.b0ne.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
@EnableConfigurationProperties(B0neProperties.class)
@ConfigurationProperties(prefix = "b0ne")
public class B0neProperties {

    private Manager manager;

    @Data
    public static class Manager {
        private String name;
        private String email;
    }
}
