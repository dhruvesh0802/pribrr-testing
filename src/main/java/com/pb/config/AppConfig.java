package com.pb.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
public class AppConfig {
    
    private static final String UTC = "UTC";
    @PostConstruct
    private void setTimeZone(){
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
    }
    
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

//    @Bean
//    public MapperFacade mapperFacade(){
//        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
//        return mapperFactory.getMapperFacade();
//    }
}
