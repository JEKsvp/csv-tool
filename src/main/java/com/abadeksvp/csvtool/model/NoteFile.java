package com.abadeksvp.csvtool.model;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class NoteFile {

    private Resource fileContent;
    private MediaType contentType;
    private String fileName;
}
