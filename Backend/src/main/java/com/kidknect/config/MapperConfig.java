package com.kidknect.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up ModelMapper.
 */
@Configuration
public class MapperConfig {

	/**
	 * Bean to create an instance of ModelMapper.
	 *
	 * @return ModelMapper instance configured with specific options.
	 */
	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// Configure ModelMapper options
		modelMapper.getConfiguration()
				// Skip null properties when mapping
				.setSkipNullEnabled(true)
				// Use strict matching strategy for field mapping
				.setMatchingStrategy(MatchingStrategies.STRICT)
				// Set a condition to skip mapping if the source value is null
				.setPropertyCondition(Conditions.isNotNull());

		return modelMapper;
	}

}
