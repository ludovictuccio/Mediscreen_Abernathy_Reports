package com.mediscreen.reports.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mediscreen.reports.domain.dto.NoteDto;
import com.mediscreen.reports.domain.dto.PatientDto;
import com.mediscreen.reports.exceptions.PatientException;
import com.mediscreen.reports.proxies.MicroserviceNotesProxy;
import com.mediscreen.reports.proxies.MicroservicePatientProxy;
import com.mediscreen.reports.util.AgeCalculator;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ReportServiceImpl.class);

    @Autowired
    private MicroservicePatientProxy microservicePatientProxy;

    @Autowired
    private MicroserviceNotesProxy microserviceNotesProxy;

    /**
     * {@inheritDoc}
     */
    public PatientDto getPatientPersonalInformations(final Long patId) {
        return microservicePatientProxy.getPatientPersonalInformations(patId);
    }

    /**
     * {@inheritDoc}
     */
    public List<NoteDto> getAllPatientsNoteDto(final Long patId) {
        return microserviceNotesProxy.getAllPatientsNoteDto(patId);
    }

    /**
     * {@inheritDoc}
     */
    public int getPatientAge(final String birthdate) {
        return AgeCalculator.getPatientAge(birthdate);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPatientsMale(final String sex) throws PatientException {
        boolean isPatientsMale = false;
        if (sex.toUpperCase().contains("M")) {
            isPatientsMale = true;
            return isPatientsMale;
        } else if (!sex.toUpperCase().contains("F")) {
            throw new PatientException(
                    "The patient doesn't contain a sex with 'M' or 'F' !");
        }
        return isPatientsMale;
    }
}
