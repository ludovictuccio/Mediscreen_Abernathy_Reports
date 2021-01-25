package com.mediscreen.reports.domain.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String lastName;

    private String firstName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String birthdate;

    private String sex;

}
