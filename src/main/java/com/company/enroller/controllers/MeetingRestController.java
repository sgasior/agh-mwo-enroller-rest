package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;

// ma byc np. meetings/1/participants/2

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

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

	// @RequestMapping(value = "", method = RequestMethod.POST)
	// public ResponseEntity<?> addParticipant(@RequestBody Participant participant)
	// {
	//
	// // czy nie istnieje
	// if (participantService.findByLogin(participant.getLogin()) != null) {
	// return new ResponseEntity("Participant with login: " + participant.getLogin()
	// + " exists",
	// HttpStatus.CONFLICT);
	// }
	//
	// // dodac
	// participantService.addParticipant(participant);
	//
	// // zwrocic
	// return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	//
	// }
	//
	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	// public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login)
	// {
	//
	// // czy istnieje
	// if (participantService.findByLogin(login) == null) {
	// return new ResponseEntity("Participant with login: " + login + "does not
	// exists", HttpStatus.NOT_FOUND);
	// }
	//
	// // dodac
	// participantService.deleteParticipantByLogin(login);
	//
	// // zwrocic
	// return new ResponseEntity<Participant>(HttpStatus.OK);
	//
	// }
	//
	// @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	// public ResponseEntity<?> updateParticipant(@PathVariable("id") String login,
	// @RequestBody Participant updatedParticipant) {
	// Participant participant = participantService.findByLogin(login);
	// if (participant == null) {
	// return new ResponseEntity(HttpStatus.NOT_FOUND);
	// }
	// participant.setPassword(updatedParticipant.getPassword());
	// participantService.update(participant);
	// return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	// }

}
