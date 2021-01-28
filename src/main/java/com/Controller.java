package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private ProfessorsRepository professorsRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping("/subjects")
    public Map<String, Object> getSubjects(Authentication authentication) {
        Map<String, Object> DTO = new LinkedHashMap<String, Object>();
        if (authentication == null) {
            DTO.put("user", null);
        }else{
            Users user = usersRepository.findByDni(authentication.getName());
            DTO.put("user", user.toDTO());
        }
        DTO.put("subjects", subjectsRepository.findAll()
                .stream()
                .map(subject -> subject.toDTO())
                .collect(toList()));
        DTO.put("registrations", registrationRepository.findAll()
                .stream()
                .map(subject -> subject.toDTO())
                .collect(toList()));
        return DTO;
    }

    @RequestMapping("/administrators")
    public Map<String, Object> getProfessors(Authentication authentication) {
        Map<String, Object> DTO = new LinkedHashMap<String, Object>();
        if (authentication == null) {
            DTO.put("user", null);
        }else{
            DTO.put("professors", professorsRepository.findAll()
                    .stream()
                    .map(subject -> subject.toDTO())
                    .collect(toList()));

        }return DTO;
    }

    @RequestMapping(path = "/subjects/{subjectId}/users", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinSubject(Authentication authentication, @PathVariable Long subjectId) {
        if (authentication == null) {
            return new ResponseEntity<>(makeMap("error", "You must have a user"), HttpStatus.FORBIDDEN);
        }else {
            Subjects subject = subjectsRepository.findById(subjectId).orElse(null);
            subjectsRepository.save(subject);
            if (subject == null) {
                return new ResponseEntity<>(makeMap("error", "Not Found"), HttpStatus.NOT_FOUND);
            } else {
                Users user = usersRepository.findByDni(authentication.getName());
                Registration newRegistration = new Registration(user, subject);
                registrationRepository.save(newRegistration);
                return new ResponseEntity<>(makeMap("regid", newRegistration.getId()), HttpStatus.CREATED);
            }
        }
    }


    public Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
