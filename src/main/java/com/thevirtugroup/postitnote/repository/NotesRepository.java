package com.thevirtugroup.postitnote.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.thevirtugroup.postitnote.model.Note;

@Repository
public class NotesRepository {
	
	private long ID = 0L;

    private Map<Long, Note> notes;

    public NotesRepository() {
    	notes = new HashMap<>();
    	Note note;
    	
    	for (int i = 0; i < 5; i++) {
    		note = new Note();
    		note.setId(newID());
    		note.setTimestamp(0L);
    		note.setName("Note #" + note.getId());
    		note.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt");
    		notes.put(note.getId(), note);
    	}
    	
    }

    public Note findById(Long id) {
        return notes.get(id);
    }

	public List<Note> getNotes() {
		return notes.values().stream()
				//.sorted() // TODO: Any particular order?
				.collect(Collectors.toList());
	}

	public void saveNote(Note note) {
		
		if (note.getId() == null) {
			note.setId(newID());
			note.setTimestamp(0L); // TODO: Set to current time?
		}
		
		// TODO: Merge changes?
		notes.put(note.getId(), note);
	}
	
	// TODO: ID Generator?
	private long newID() {
		return ID++;
	}

}
