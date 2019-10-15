package com.athena;

import com.athena.init.InitExecutor;
import java.util.concurrent.CompletableFuture;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mukong
 */
@SpringBootApplication
public class AthenaTransportApplication {

	public static void main(String[] args) {
		triggerAthenaInit();
		SpringApplication.run(AthenaTransportApplication.class, args);
	}

	private static void triggerAthenaInit() {
		CompletableFuture.runAsync(InitExecutor::doInit);
	}
}
