package br.nom.figueiredo.sergio.cogito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CogitoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CogitoApplication.class, args);
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
