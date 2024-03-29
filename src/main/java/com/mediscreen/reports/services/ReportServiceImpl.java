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
import com.mediscreen.reports.util.Assessment;

@PropertySource("triggerTerms")
@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ReportServiceImpl.class);

    @Autowired
    private MicroservicePatientProxy microservicePatientProxy;

    @Autowired
    private MicroserviceNotesProxy microserviceNotesProxy;

    @Autowired
    private AgeCalculator ageCalculator;

    private String[] triggerTerms;

    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int THIRTY = 30;

    public ReportServiceImpl(final MicroservicePatientProxy patientProxy,
            final MicroserviceNotesProxy notesProxy,
            @Value("${triggerTerms}") final String[] triggerTermsList,
            final AgeCalculator ageCalcul) {
        this.microservicePatientProxy = patientProxy;
        this.microserviceNotesProxy = notesProxy;
        this.triggerTerms = triggerTermsList;
        this.ageCalculator = ageCalcul;
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
    public List<NoteDto> getAllPatientsNoteDto(final String lastName,
            final String firstName) {
        return microserviceNotesProxy.getAllPatientsNoteDto(lastName,
                firstName);
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

        // retrieve all patient's concatenate notes
        String notesInString = allPatientsNotes.stream().map(NoteDto::getNote)
                .map(String::toUpperCase).map(String::trim)
                .map(string -> getStringIgnoringAccents(string))
                .collect(Collectors.joining());

        // count all trigger terms in theses concatenate notes
        long triggerTermsNumber = Arrays.stream(triggerTerms)
                .map(String::toUpperCase).filter(notesInString::contains)
                .distinct().count();

        return triggerTermsNumber;
    }

    /**
     * Method used to ignore accents.
     *
     * @param string
     * @return pattern.matcher
     */
    private static String getStringIgnoringAccents(final String string) {
        String strTemp = Normalizer.normalize(string, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(strTemp).replaceAll("");
    }

    /**
     * Method used to calculate patient's age.
     *
     * @param birthdate
     * @return int age
     */
    public int getAge(final String birthdate) {
        int age = ageCalculator.getPatientAge(birthdate);
        return age;
    }

    /**
     * {@inheritDoc}
     *
     */
    public Assessment getDiabeteAssessment(final PatientDto patient,
            final List<NoteDto> allPatientsNotes) throws PatientException {

        long triggerTermsNumber = getTriggerTermsNumber(allPatientsNotes);

        int patientAge = getAge(patient.getBirthdate());

        // return true if male, false if female
        boolean isPatientMale = isPatientsMale(patient.getSex());

        Assessment assessment = Assessment.None;

        if (patientAge >= THIRTY && triggerTermsNumber >= TWO
                && triggerTermsNumber < SIX) {
            assessment = Assessment.Borderline;
        } else if ((isPatientMale && (patientAge < THIRTY
                && (triggerTermsNumber == THREE || triggerTermsNumber == FOUR)))
                || (!isPatientMale && patientAge < THIRTY
                        && (triggerTermsNumber >= FOUR
                                && triggerTermsNumber <= SIX))
                || (patientAge >= THIRTY && (triggerTermsNumber == SIX
                        || triggerTermsNumber == SEVEN))) {
            assessment = Assessment.InDanger;
        } else if ((isPatientMale
                && (patientAge < THIRTY && triggerTermsNumber >= FIVE))
                || (!isPatientMale && patientAge < THIRTY
                        && triggerTermsNumber >= SEVEN)
                || (patientAge >= THIRTY && triggerTermsNumber >= EIGHT)) {
            assessment = Assessment.EarlyOnset;
        }
        return assessment;
    }

    /**
     * {@inheritDoc}
     *
     */
    public Report getDiabeteReport(final Long patId) throws PatientException {

        try {
            PatientDto patient = getPatientPersonalInformations(patId);

            List<NoteDto> allNotes = getAllPatientsNoteDto(
                    patient.getLastName(), patient.getFirstName());

            Assessment diabeteAssessment = getDiabeteAssessment(patient,
                    allNotes);

            Report report = new Report(patId, patient.getFirstName(),
                    patient.getLastName(), getAge(patient.getBirthdate()),
                    diabeteAssessment);

            return report;
        } catch (NullPointerException np) {
            LOGGER.error(
                    "Report microservice - No patient found with id:" + patId);
        }
        return null;
    }
}
