package com;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Subjects {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @OneToMany(mappedBy="subject", fetch= FetchType.EAGER)
    private Set<Registration> registrations;

    private String nombre;

    private LocalDateTime horario;

    private String descripcion;

    private Integer cupoMaximo;

    public Subjects () {}

    public Subjects (String nombre, LocalDateTime horario, String descripcion, Integer cupoMaximo) {
        this.nombre = nombre;
        this.horario = horario;
        this.descripcion = descripcion;
        this.cupoMaximo = cupoMaximo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public long getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<Registration> registrations) {
        this.registrations = registrations;
    }

    public void addRegistration(Registration registration){
        registration.setSubjects(this);
        registrations.add(registration);
    }

    public Map<String, Object> toDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", id);
        dto.put("materia", nombre);
        dto.put("horario", horario);
        dto.put("descripcion", descripcion);
        dto.put("cupomaximo", cupoMaximo);
        return dto;
    }
}
