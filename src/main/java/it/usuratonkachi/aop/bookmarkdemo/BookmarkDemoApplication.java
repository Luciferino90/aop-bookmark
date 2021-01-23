package it.usuratonkachi.aop.bookmarkdemo;

import it.usuratonkachi.aop.bookmarkdemo.runner.RunnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class BookmarkDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmarkDemoApplication.class, args);
	}

	private final RunnerService runnerService;
	private final ConfigurableApplicationContext applicationContext;

	@Bean
	public CommandLineRunner runner() {
		return args -> {
			List<String> addresses = List.of("ciaoMondo@pec.it");

			Flux.fromIterable(addresses)
					.flatMap(runnerService::runCleverBoy)
					.collectList()
					.doFinally(signal -> SpringApplication.exit(applicationContext, () -> 0))
					.subscribe();

		};
	}

}
