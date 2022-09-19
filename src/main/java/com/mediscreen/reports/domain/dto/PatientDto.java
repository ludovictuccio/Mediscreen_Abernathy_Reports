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

    public PatientDto(final String plastName, final String pfirstName,
            final String pbirthdate, String psex) {
        super();
        this.lastName = plastName;
        this.firstName = pfirstName;
        this.birthdate = pbirthdate;
        this.sex = psex;
    }

}
