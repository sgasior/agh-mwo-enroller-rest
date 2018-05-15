package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {

		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
		if (meetingService.findById(meeting.getId()) != null) {
			return new ResponseEntity("Unable to create. A meeting with id: " + meeting.getId() + " already exist.",
					HttpStatus.CONFLICT);
		}
		meetingService.addMeeting(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);

	}

	@RequestMapping(value = "/{meetingId}/participants/{participantLogin}", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantToMeeting(@PathVariable("meetingId") Long id,
			@PathVariable("participantLogin") String login) {

		Meeting meeting = meetingService.findById(id);
		Participant participant = participantService.findByLogin(login);

		if (meeting == null || participant == null) {
			return new ResponseEntity("Unable to add participant with login: " + login + " to the meeting with id: "
					+ id + " because meeting or participant does not exist.", HttpStatus.NOT_FOUND);
		}
		meetingService.addParicipantToMeeting(meeting, participant);
		return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "{meetingId}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipantsOfMeeting(@PathVariable("meetingId") Long id) {

		Meeting meeting = meetingService.findById(id);

		if (meeting == null) {
			return new ResponseEntity("Unable to find participants of metting with id: " + id
					+ " because meeting with this id does not exist.", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(meeting.getParticipants(), HttpStatus.OK);

	}

}