package pl.marek.infrastructure;

import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marek.domain.Film;
import pl.marek.domain.FilmDto;
import pl.marek.domain.FilmFacade;

@RestController
@RequestMapping(value = "films")
@AllArgsConstructor
public class FilmController {

    private final FilmFacade filmFacade;

    @GetMapping
    public ResponseEntity<Page<FilmDto>> getFilms(
            @PageableDefault @SortDefault.SortDefaults(@SortDefault(sort = "name", direction = Sort.Direction.ASC)) Pageable pageable,
            @QuerydslPredicate(root = Film.class) Predicate predicate
    ) {

        return ResponseEntity.ok(filmFacade.findAll(predicate, pageable));
    }
}
