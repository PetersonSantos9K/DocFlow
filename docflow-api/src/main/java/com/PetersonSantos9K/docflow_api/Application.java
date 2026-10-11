package com.PetersonSantos9K.docflow_api;

import com.PetersonSantos9K.docflow_api.ingestion.application.IngestionOrchestrator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties
public class Application {


	public static void main(String[] args) {


		var builder = new SpringApplicationBuilder(Application.class);
		builder.run(args);

		ApplicationContext applicationContext = builder.context();
		IngestionOrchestrator bean = applicationContext.getBean(IngestionOrchestrator.class);
		bean.ingest("https://github.com/PetersonSantos9K/repository-teste-docflow.git");
	}

}
