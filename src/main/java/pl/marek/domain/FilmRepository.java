package pl.marek.domain;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.MultiValueBinding;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

import java.time.Instant;
import java.util.*;

interface FilmRepository extends MongoRepository<Film, String>, QuerydslPredicateExecutor<Film>, QuerydslBinderCustomizer<QFilm> {

    Page<Film> findAll(final Predicate predicate, final Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QFilm root) {
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>)
                StringExpression::containsIgnoreCase);
        bindings.bind(Instant.class).all((MultiValueBinding<DateTimePath<Instant>, Instant>)
                (path, values) -> Optional.ofNullable(getTemporalCondition(path, values)));

        bindings.excluding(root.id);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default BooleanExpression getTemporalCondition(final TemporalExpression path, final Collection<? extends Comparable> values) {
        if (values.size() == 2) {
            final List<? extends Comparable> dateValues = new ArrayList<>(values);
            Collections.sort(dateValues);
            return path.between(dateValues.get(0), dateValues.get(1));
        } else {
            return path.goe(values.iterator().next());
        }
    }
}
