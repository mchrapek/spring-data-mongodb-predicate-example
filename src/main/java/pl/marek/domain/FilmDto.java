package pl.marek.domain;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class FilmDto {

    String name;

    String director;

    Instant releaseDate;

    static FilmDto from(Film film) {
        return FilmDto.builder()
                .name(film.getName())
                .director(film.getDirector())
                .releaseDate(film.getReleaseDate())
                .build();
    }
}
