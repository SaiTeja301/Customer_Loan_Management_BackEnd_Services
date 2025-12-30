package com.SprCustomers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperExp {
	@Bean
    ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
