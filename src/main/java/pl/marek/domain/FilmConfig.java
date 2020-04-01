package pl.marek.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FilmConfig {

    @Bean
    FilmFacade filmFacade(FilmRepository filmRepository) {
        return new FilmFacade(filmRepository);
    }
}
