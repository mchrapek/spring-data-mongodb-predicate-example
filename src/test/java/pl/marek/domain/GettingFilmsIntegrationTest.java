package pl.marek.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import pl.marek.app.Application;

import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class GettingFilmsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository filmRepository;

    private final static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    @BeforeEach
    public void setup() throws Exception {
        filmRepository.deleteAll();

        filmRepository.save(Film.builder()
                .name("Inception")
                .director("Christopher Nolan")
                .releaseDate(SDF.parse("08/07/2010").toInstant())
                .build());

        filmRepository.save(Film.builder()
                .name("Shutter Island")
                .director("Martin Scorsese")
                .releaseDate(SDF.parse("13/02/2010").toInstant())
                .build());

        filmRepository.save(Film.builder()
                .name("The Departed")
                .director("Martin Scorsese")
                .releaseDate(SDF.parse("26/10/2006").toInstant())
                .build());
    }

    @Test
    public void shouldReturnFirstPage() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value("3"))
                .andExpect(jsonPath("$.numberOfElements").value("3"))
                .andExpect(jsonPath("$.totalPages").value("1"))
                .andReturn();
    }

    @Test
    public void shouldReturnFilmsByDirector() throws Exception {
        mockMvc.perform(get("/films?director=Nolan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value("1"))
                .andReturn();
    }

    @Test
    public void shouldReturnFilmsBetweenDates() throws Exception {
        mockMvc.perform(get("/films?releaseDate=2010-01-01T00:00:00.000Z&releaseDate=2010-11-01T00:00:00.000Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements", is(2)))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(content().string(containsString("Inception")))
                .andExpect(content().string(containsString("Shutter Island")))
                .andExpect(content().string(not(containsString("The Departed"))))
                .andReturn();
    }

    @Test
    public void shouldReturnFilmsGoeDates() throws Exception {
        mockMvc.perform(get("/films?releaseDate=2010-06-01T00:00:00.000Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements", is(1)))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().string(containsString("Inception")))
                .andExpect(content().string(not(containsString("Shutter Island"))))
                .andExpect(content().string(not(containsString("The Departed"))))
                .andReturn();
    }
}