package pl.marek.domain;

import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
public class FilmFacade {

    private final FilmRepository repository;

    public Page<FilmDto> findAll(Predicate predicate, Pageable pageable) {
        return repository
                .findAll(predicate, pageable)
                .map(FilmDto::from);
    }
}
