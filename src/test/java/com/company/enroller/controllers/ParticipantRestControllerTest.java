package com.company.enroller.controllers;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(ParticipantRestController.class)
public class ParticipantRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MeetingService meetingService;

	@MockBean
	private ParticipantService participantService;

	@Test
	public void getParticipants() throws Exception {
		Participant participant = new Participant();
		participant.setLogin("testlogin");
		participant.setPassword("testpassword");

		Collection<Participant> allParticipants = singletonList(participant);
		given(participantService.getAll()).willReturn(allParticipants);

		mvc.perform(get("/participants").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].login", is(participant.getLogin())));
	}

	@Test
	public void getParticipant() throws Exception {
		Participant participant = new Participant();
		participant.setLogin("testlogin");
		participant.setPassword("testpassword");
		given(participantService.findByLogin(participant.getLogin())).willReturn(participant);
		mvc.perform(get("/participants/testlogin").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string("{\"login\":\"testlogin\",\"password\":\"testpassword\"}"));
	}

	@Test
	public void getNotExistingParticipant() throws Exception {

		mvc.perform(get("/participants/testlogin").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(content().string(""));
	}

	@Test
	public void deleteParticipant() throws Exception {

		Participant participant = new Participant();
		participant.setLogin("testlogin");
		participant.setPassword("testpassword");
		given(participantService.findByLogin(participant.getLogin())).willReturn(participant);
		participantService.delete(participant);
		mvc.perform(delete("/participants/testlogin").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent()).andExpect(content().string(""));
	}

	@Test
	public void deleteNotExistingParticipant() throws Exception {

		mvc.perform(delete("/participants/testlogin").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(content().string(""));
	}

	@Test
	public void addParticipant() throws Exception {

		Participant participant = new Participant();
		participant.setLogin("testlogin");
		participant.setPassword("testpassword");
		String inputJSON = new ObjectMapper().writeValueAsString(participant);
		// {\"login\":\"testlogin\",\"password\":\"testpassword\ tworzy to takiego json
		mvc.perform(post("/participants").content(inputJSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string(inputJSON)).andExpect(status().isCreated());
	}

}
