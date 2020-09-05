package io.zipcoder.crudapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class PersonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(personController)
                .build();
    }

    @Test
    public void getPersonList() throws Exception {
        List<Person> people = Arrays.asList(
                new Person(1L,"Harley", "Ozwald"),
                new Person(2L,"Yu", "Matz")
        );

        when(personService.getPersonList()).thenReturn(people);

        mockMvc.perform(get("/people"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Harley")))
                .andExpect(jsonPath("$[0].lastName", is("Ozwald")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Yu")))
                .andExpect(jsonPath("$[1].lastName", is("Matz")));

        verify(personService, times(1)).getPersonList();
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getPersonById() throws Exception {
        Person person = new Person(1L,"Harley", "Ozwald");

        when(personService.getPerson(1L)).thenReturn(person);

        mockMvc.perform(get("/people/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Harley")))
                .andExpect(jsonPath("$.lastName", is("Ozwald")));

        verify(personService, times(1)).getPerson(1L);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getPersonByIdFail() throws Exception {
        when(personService.getPerson(7L)).thenReturn(null);

        mockMvc.perform(get("/people/{id}", 7L))
                .andExpect(status().isNotFound());

        verify(personService, times(1)).getPerson(7L);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void createPerson() throws Exception {
        Person person = new Person(1L, "Harley", "Ozwald");

        when(personService.createPerson(person)).thenReturn(person);
        System.out.println("The JSON is: " + asJsonString(person));
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(person)))
                .andExpect(status().isCreated());

        verify(personService, times(1)).createPerson(person);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void updatePerson() throws Exception {
        Person person = new Person(1L, "Harley", "Ozwald");

        when(personService.updatePerson(person)).thenReturn(person);

        mockMvc.perform(
                put("/people/{id}", person.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(person)))
                .andExpect(status().isCreated());

        verify(personService, times(1)).getPerson(person.getId());
        verify(personService, times(1)).updatePerson(person);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void deletePerson() throws Exception {
        Person person = new Person(1L, "Harley", "Ozwald");

        when(personService.deletePerson(person.getId())).thenReturn(person);

        mockMvc.perform(
                delete("/people/{id}", person.getId()))
                .andExpect(status().isNoContent());

        verify(personService, times(1)).deletePerson(person.getId());
        verifyNoMoreInteractions(personService);
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}