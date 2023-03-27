package com.thevirtugroup.postitnote.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thevirtugroup.postitnote.model.Note;
import com.thevirtugroup.postitnote.repository.NotesRepository;

/**
 */
@RestController
public class NoteController {
	
	@Autowired
	protected NotesRepository notesRepository;
	
	@RequestMapping(path = "/api/notes/", method = RequestMethod.GET)
	public List<Note> getNotes() {
		// TODO: Wrap in response object for paging etc.?
		
		return notesRepository.getNotes();
	}
	
	@RequestMapping(path = "/api/notes/{id}/", method = RequestMethod.GET)
	public Note getNote(@PathVariable Long id) {
		// TODO: Error handling for non-existing id?
		
		return notesRepository.findById(id);
	}
	
	@RequestMapping(path = "/api/notes/new/", method = RequestMethod.POST)
	public void createNote(@RequestBody Note note) {
		// TODO: Add service layer between controller and repository?
		
		notesRepository.saveNote(note);
		
		// TODO: Notify client on server errors?
	}
	
	@RequestMapping(path = "/api/notes/{id}/", method = RequestMethod.POST)
	public void editNote(@RequestBody Note note) {
		// TODO: Server-side validations?
		
		notesRepository.saveNote(note);
	}
	
}
