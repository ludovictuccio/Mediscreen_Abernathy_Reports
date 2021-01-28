package com.mediscreen.reports.services;

import java.util.List;

import com.mediscreen.reports.domain.Report;
import com.mediscreen.reports.domain.dto.NoteDto;
import com.mediscreen.reports.domain.dto.PatientDto;
import com.mediscreen.reports.exceptions.PatientException;
import com.mediscreen.reports.util.Assessment;

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
     * @param lastName
     * @param fisrtName
     * @return NoteDto list
     */
    List<NoteDto> getAllPatientsNoteDto(final String lastName,
            final String firstName);

    /**
     * Method used to determinate if patient's is male or female.
     *
     * @param sex
     * @return boolean isPatientMale
     * @throws PatientException
     */
    boolean isPatientsMale(final String sex) throws PatientException;

    /**
     * Method used to determine the number of patient's trigger terms.
     *
     * @param allPatientsNotes dto
     * @return long triggerTermsNumber the number of patient's trigger terms
     */
    long getTriggerTermsNumber(final List<NoteDto> allPatientsNotes);

    /**
     * Method used to determine the patient's diabetes assessment.
     *
     * @param patient          dto
     * @param allPatientsNotes
     * @return diabete assessment
     * @throws PatientException
     */
    Assessment getDiabeteAssessment(final PatientDto patient,
            final List<NoteDto> allPatientsNotes) throws PatientException;

    /**
     * Method used to get the patient's diabetes report.
     *
     * @param long patId the patient's id
     * @return the diabete report
     * @throws PatientException
     */
    Report getDiabeteReport(final Long patId) throws PatientException;
}
