package com.abadeksvp.csvtool.controller;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abadeksvp.csvtool.model.NoteFile;
import com.abadeksvp.csvtool.model.NoteFilter;
import com.abadeksvp.csvtool.service.NotesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notes/csv")
@RequiredArgsConstructor
public class NoteController {

    private final NotesService notesService;

    @PostMapping
    public void upload(MultipartFile file) {
        notesService.uploadNotes(file);
    }

    @GetMapping
    public ResponseEntity<Resource> download(
            @RequestParam(value = "code", required = false) String code) throws IOException {
        NoteFile file = notesService.downloadNotes(new NoteFilter(code));
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getFileName())
                .contentType(file.getContentType())
                .body(new InputStreamResource(file.getFileContent().getInputStream()));
    }

    @DeleteMapping
    public void delete() {
        notesService.deleteNotes();
    }
}
