package com;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="users_id")
    private Users user;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="subjects_id")
    private Subjects subject;

    public Registration (){}

    public Registration(Users user, Subjects subject){
        this.user = user;
        this.subject = subject;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUsers() {
        return user;
    }

    public void setUsers(Users users) {
        this.user = users;
    }

    public Subjects getSubjects() {
        return subject;
    }

    public void setSubjects(Subjects subjects) {
        this.subject = subjects;
    }

    public Map<String, Object> toDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", id);
        dto.put("user", user.toDTO());
        dto.put("subject", subject.toDTO());
        return dto;
    }
}
