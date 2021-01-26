package com.mediscreen.reports.services;

import java.util.List;

import com.mediscreen.reports.domain.dto.NoteDto;
import com.mediscreen.reports.domain.dto.PatientDto;
import com.mediscreen.reports.exceptions.PatientException;

public interface ReportService {

    /**
     * Method used to get patient's personal informations, with a dto.
     *
     * @param patId
     * @return PatientDto
     */
    PatientDto getPatientPersonalInformations(final Long patId);

    /**
     * Method used to get patient's notes, with a dto.
     *
     * @param patId
     * @return NoteDto list
     */
    List<NoteDto> getAllPatientsNoteDto(final Long patId);

    /**
     * Method used to get the patient's age, to determine with his birthdate.
     *
     * @param birthdate
     * @return int age the patient's age
     */
    int getPatientAge(final String birthdate);

    /**
     * Method used to determinate if patient's is male or female.
     *
     * @param sex
     * @return boolean isPatientMale
     * @throws PatientException
     */
    boolean isPatientsMale(final String sex) throws PatientException;
}
