package xyz.tumit.b3agle.b0ne.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "str0ng")
public class Str0ngProperties {
    private String dbUser;
    private String dbPwd;
}
