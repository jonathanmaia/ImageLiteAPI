package br.com.eunati.imageliteapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import br.com.eunati.imageliteapi.domain.entity.Image;
import br.com.eunati.imageliteapi.domain.enums.ImageExtension;
import br.com.eunati.imageliteapi.infra.repository.ImageRepository;

@SpringBootApplication
@EnableJpaAuditing
public class ImageliteapiApplication {

	@Bean
	public CommandLineRunner commandLineRunner(@Autowired ImageRepository repository) {
		return args -> {
			Image image = Image.builder().
				extension(ImageExtension.PNG).
				name("myimage").
				tags("testetag").
				size(1000L).
				build();
			
				repository.save(image);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ImageliteapiApplication.class, args);
	}

}
