package com.PetersonSantos9K.docflow_api;

import com.PetersonSantos9K.docflow_api.ingestion.source.SourceProviderValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {


	public static void main(String[] args) {


		var builder = new SpringApplicationBuilder(Application.class);
		builder.run(args);

		ApplicationContext applicationContext = builder.context();
		var sourceProviderValidation = applicationContext.getBean(SourceProviderValidation.class);
		sourceProviderValidation.validation("https://github.com/user/repo");

	}

}
