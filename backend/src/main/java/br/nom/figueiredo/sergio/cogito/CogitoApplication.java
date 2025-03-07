package br.nom.figueiredo.sergio.cogito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CogitoApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder builder =  new SpringApplicationBuilder(CogitoApplication.class);
//		builder.headless(false); (seria para permitir usar /includegraphics{path}. Porém, não funciona no Raspberry PI porque não tem display no modo server.
		builder.run(args);
	}

//	@Value("${spring.datasource.maximum-pool-size}")
//	private int connectionPoolSize;

//	@Bean
//	public Scheduler jdbcScheduler() {
//		return Schedulers.fromExecutor(Executors.newFixedThreadPool(connectionPoolSize));
//	}

//	@Bean
//	public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
//		return new TransactionTemplate(transactionManager);
//	}
}
