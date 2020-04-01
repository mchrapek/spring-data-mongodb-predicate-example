package pl.marek.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document
public class Film {

    @Id
    private String id;

    private String name;

    private String director;

    private Instant releaseDate;
}
