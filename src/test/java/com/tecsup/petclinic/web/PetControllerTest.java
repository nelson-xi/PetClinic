package com.tecsup.petclinic.web;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.domain.Pet;

/**
 * 
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class PetControllerTest {

    private static final ObjectMapper om = new ObjectMapper();
    
	@Autowired
	private MockMvc mockMvc;


	@Test
	public void testGetPets() throws Exception {

		//int NRO_RECORD = 73;
		int ID_FIRST_RECORD = 1;

		this.mockMvc.perform(get("/pets"))
					.andExpect(status().isOk())
					.andExpect(content()
					.contentType(MediaType.APPLICATION_JSON_UTF8))
		//		    .andExpect(jsonPath("$", hasSize(NRO_RECORD)))
					.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
	}
	

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFindPetOK() throws Exception {

		String NAME_PET = "Leo";
		int TYPE_ID = 1;
		int OWNER_ID = 1;
		Date DATE = new SimpleDateFormat("yyyy-MM-dd").parse("2000-09-07");

		mockMvc.perform(get("/pets/1"))  // Object must be BASIL 
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is(NAME_PET)))
				.andExpect(jsonPath("$.typeId", is(TYPE_ID)))
				.andExpect(jsonPath("$.ownerId", is(OWNER_ID)))
				//.andExpect(jsonPath("$.birthDate", is("2000-09-07")));
				.andExpect(jsonPath("$.birthDate", is(new SimpleDateFormat("yyyy-MM-dd").format(DATE))));

	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFindPetKO() throws Exception {

		mockMvc.perform(get("/pets/666"))
				.andExpect(status().isNotFound());

	}

	/**
	 * 
	 * @throws Exception
	 */
    @Test
    public void testCreatePet() throws Exception {
		
    	String NAME_PET = "Beethoven";
		int TYPE_ID = 1;
		int OWNER_ID = 1;
		Date DATE = new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-20");
		
		Pet newPet = new Pet(NAME_PET, TYPE_ID, OWNER_ID, DATE);
	
	    mockMvc.perform(post("/pets")
	            .content(om.writeValueAsString(newPet))
	            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
	            .andDo(print())
	            .andExpect(status().isCreated())
	            //.andExpect(jsonPath("$.id", is(1)))
	            .andExpect(jsonPath("$.name", is(NAME_PET)))
	            .andExpect(jsonPath("$.typeId", is(TYPE_ID)))
	            .andExpect(jsonPath("$.ownerId", is(OWNER_ID)))
	    		.andExpect(jsonPath("$.birthDate", is(new SimpleDateFormat("yyyy-MM-dd").format(DATE))));
    
	}

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDeletePet() throws Exception {

    	String NAME_PET = "Beethoven3";
		int TYPE_ID = 1;
		int OWNER_ID = 1;
		Date DATE = new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-20");
		
		Pet newPet = new Pet(NAME_PET, TYPE_ID, OWNER_ID, DATE);
		
		ResultActions mvcActions = mockMvc.perform(post("/pets")
	            .content(om.writeValueAsString(newPet))
	            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
	            .andDo(print())
	            .andExpect(status().isCreated());
	            
		String response = mvcActions.andReturn().getResponse().getContentAsString();

		Integer id = JsonPath.parse(response).read("$.id");

        mockMvc.perform(delete("/pets/" + id ))
                /*.andDo(print())*/
                .andExpect(status().isOk());
    }
    
}
