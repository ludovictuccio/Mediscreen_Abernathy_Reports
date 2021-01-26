package com.mediscreen.reports.services;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.mediscreen.reports.domain.Report;
import com.mediscreen.reports.domain.dto.NoteDto;
import com.mediscreen.reports.domain.dto.PatientDto;
import com.mediscreen.reports.exceptions.PatientException;
import com.mediscreen.reports.proxies.MicroserviceNotesProxy;
import com.mediscreen.reports.proxies.MicroservicePatientProxy;
import com.mediscreen.reports.util.AgeCalculator;

@PropertySource("triggerTerms")
@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ReportServiceImpl.class);

    @Autowired
    private MicroservicePatientProxy microservicePatientProxy;

    @Autowired
    private MicroserviceNotesProxy microserviceNotesProxy;

    private String[] triggerTerms;

    public ReportServiceImpl(final MicroservicePatientProxy patientProxy,
            final MicroserviceNotesProxy notesProxy,
            @Value("${triggerTerms}") final String[] triggerTermsList) {
        this.microservicePatientProxy = patientProxy;
        this.microserviceNotesProxy = notesProxy;
        this.triggerTerms = triggerTermsList;
    }

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

    /**
     * {@inheritDoc}
     */
    public long getTriggerTermsNumber(final List<NoteDto> allPatientsNotes) {

        // retrieve all patient's trigger terms notes
        String notesInString = allPatientsNotes.stream().map(NoteDto::getNote)
                .map(String::toUpperCase).map(String::trim)
                .map(string -> getStringIgnoringAccents(string))
                .collect(Collectors.joining());

        // count all trigger terms
        long triggerTermsNumber = Arrays.stream(triggerTerms)
                .map(String::toUpperCase).filter(notesInString::contains)
                .distinct().count();

        return triggerTermsNumber;
    }

    /**
     * Method used to ignore accents.
     *
     * @param string
     */
    private static String getStringIgnoringAccents(final String string) {
        String strTemp = Normalizer.normalize(string, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(strTemp).replaceAll("");
    }

    /**
     * {@inheritDoc}
     */
    public String getDiabeteAssessment(final PatientDto patient,
            final List<NoteDto> allPatientsNotes) {
        return null;

    }

    /**
     * {@inheritDoc}
     */
    public Report getDiabeteReport(final PatientDto patient) {
        return null;

    }
}
