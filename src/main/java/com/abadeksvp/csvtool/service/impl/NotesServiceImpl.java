package com.abadeksvp.csvtool.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abadeksvp.csvtool.model.NoteFile;
import com.abadeksvp.csvtool.model.NoteFilter;
import com.abadeksvp.csvtool.model.entity.Note;
import com.abadeksvp.csvtool.repository.NoteRepository;
import com.abadeksvp.csvtool.service.NoteConverter;
import com.abadeksvp.csvtool.service.NotesService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

//Current implementation works only for relative small files, for large files it's better to use batch processing.
@Service
@RequiredArgsConstructor
public class NotesServiceImpl implements NotesService {

    private final NoteConverter converter;
    private final NoteRepository repository;

    @Override
    @SneakyThrows
    public void uploadNotes(MultipartFile file) {
        Collection<Note> notes = converter.parseNotes(file.getInputStream());
        repository.saveAll(notes);
    }

    @Override
    public NoteFile downloadNotes(NoteFilter filter) {
        List<Note> notes = findNoted(filter);
        return converter.toFile(notes);
    }

    @Override
    public void deleteNotes() {
        repository.deleteAll();
    }

    // In the case of extension of filtering capabilities, implementation can be changed to use criteria API.
    private List<Note> findNoted(NoteFilter filter) {
        if (filter.code() == null) {
            return repository.findAll();
        } else {
            Example<Note> example = Example.of(Note.builder().code(filter.code()).build());
            return repository.findAll(example);
        }
    }
}
