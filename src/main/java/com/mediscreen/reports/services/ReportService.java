package com.mediscreen.reports.services;

import java.util.List;

import com.mediscreen.reports.domain.dto.NoteDto;
import com.mediscreen.reports.domain.dto.PatientDto;

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
}
