package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.Grade;
import com.schoolhealth.schoolmedical.service.grade.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/greades")
public class GradeController {

    private GradeService service;

    @GetMapping
    public List<Grade> listAll() {
        return service.getAllGrades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getGradeById(id));
    }

    @PostMapping
    public ResponseEntity<Grade> create(@RequestBody Grade grade) {
        Grade created = service.createGrade(grade);
        return ResponseEntity
                .status(201)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> update(
            @PathVariable Long id,
            @RequestBody Grade grade) {
        return ResponseEntity.ok(service.updateGrade(id, grade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
