package com.mediscreen.reports.services;

import com.mediscreen.reports.domain.dto.PatientDto;

public interface ReportService {

    PatientDto getPatientPersonalInformations(final Long patId);
}
