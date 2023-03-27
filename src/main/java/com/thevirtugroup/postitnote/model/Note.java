package com.thevirtugroup.postitnote.model;

public class Note {

    private Long id;
    private Long timestamp;
    private String name;
    private String text;
    
    // TODO: Additional fields? Link to user?

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
