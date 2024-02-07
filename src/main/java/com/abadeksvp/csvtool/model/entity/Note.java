package com.abadeksvp.csvtool.model.entity;


import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    // Use code as primary key for simplicity of saving data
    @Id
    private String code;

    private String source;
    private String codeListCode;
    private String displayValue;
    private String longDescription;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long sortingPriority;
}
