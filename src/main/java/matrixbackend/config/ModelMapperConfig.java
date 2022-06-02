package matrixbackend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

    @Configuration
    public class ModelMapperConfig {

        @Bean(name = "modelMapper")
        public ModelMapper getModelMapper() {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper;
        }
    }

