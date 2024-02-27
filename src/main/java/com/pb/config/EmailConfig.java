package com.pb.config;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailConfig {
     String port;
     String username;
     String password;
     String host;
     String fromEmail;
    
    public EmailConfig getEmailCredential(){
        return EmailConfig.builder()
                .port(port)
                .username(username)
                .password(password)
                .host(host)
                .fromEmail(fromEmail)
                .build();
    }
}
