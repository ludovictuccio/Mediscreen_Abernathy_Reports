package com.mediscreen.reports.domain;

import java.io.Serializable;

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

    public Long patId;

    public String patFirstName;

    public String patLastName;

    public int patAge;

    public String diabeteAssessment;

}
