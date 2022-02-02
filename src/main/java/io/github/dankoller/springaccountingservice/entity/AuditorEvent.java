package io.github.dankoller.springaccountingservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AuditorEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String action;
    private String subject;
    private String object;
    private String path;

    // Constructors
    public AuditorEvent(String action, String subject, String object, String path) {
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    public AuditorEvent() {
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
