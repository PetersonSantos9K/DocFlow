package com.PetersonSantos9K.docflow_api;

import com.PetersonSantos9K.docflow_api.ingestion.IngestionContext;
import com.PetersonSantos9K.docflow_api.ingestion.IngestionService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {


	public static void main(String[] args) {


		var builder = new SpringApplicationBuilder(Application.class);
		builder.run(args);

		ApplicationContext applicationContext = builder.context();
		IngestionService bean = applicationContext.getBean(IngestionService.class);
		bean.ingest("https://github.com/PetersonSantos9K/repository-teste-docflow.git");
	}

}
