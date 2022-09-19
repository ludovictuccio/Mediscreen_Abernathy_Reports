package com.mediscreen.reports.domain;

import java.io.Serializable;

import com.mediscreen.reports.util.Assessment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The patient's id
     */
    private Long patId;

    /**
     * The patient's firstName
     */
    private String patFirstName;

    /**
     * The patient's lastName
     */
    private String patLastName;

    /**
     * The patient's age
     */
    private int patAge;

    /**
     * The patient's diabetes assessment
     */
    private Assessment diabeteAssessment;

}
