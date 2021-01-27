package com.mediscreen.reports.domain.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class PatientDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String birthdate;

    @Getter
    @Setter
    private String sex;

    public PatientDto(String lastName, String firstName, String birthdate,
            String sex) {
        super();
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthdate = birthdate;
        this.sex = sex;
    }

}
