package com.mediscreen.reports.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mediscreen.reports.domain.Report;
import com.mediscreen.reports.domain.dto.NoteDto;
import com.mediscreen.reports.domain.dto.PatientDto;
import com.mediscreen.reports.exceptions.PatientException;
import com.mediscreen.reports.proxies.MicroserviceNotesProxy;
import com.mediscreen.reports.proxies.MicroservicePatientProxy;
import com.mediscreen.reports.util.Assessment;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    /*
     * TO USE THIS TESTS :
     *
     * The age is normally calculated based on the current date. To change with
     * a fixed date, you must change the method called from:
     *
     * ReportServiceImpl --> getPatientAge method
     */

    private ReportServiceImpl reportService;

    @Mock
    private MicroservicePatientProxy microservicePatientProxy;

    @Mock
    private MicroserviceNotesProxy microserviceNotesProxy;

    private List<NoteDto> notesList;

    private static String date29 = "1991-01-01";
    private static String date30 = "1990-01-01";
    private static String date31 = "1989-01-01";

    private static PatientDto patientGeneric1 = new PatientDto(1L, "Generic1",
            "Patient1", date29, "M");
    private static PatientDto patientGeneric2 = new PatientDto(2L, "Generic2",
            "Patient2", date30, "F");

    private static PatientDto patientMale29;
    private static PatientDto patientMale30;
    private static PatientDto patientMale31;
    private static PatientDto patientFemale29;
    private static PatientDto patientFemale30;
    private static PatientDto patientFemale31;

    private String term1 = "Hemoglobine A1C";
    private String term2 = "Microalbumine";
    private String term3 = "Taille";
    private String term4 = "Poids";
    private String term5 = "Fumeur";
    private String term6 = "Anormal";
    private String term7 = "Cholesterol";
    private String term8 = "Vertige";
    private String term9 = "Rechute";

    static {
        patientMale29 = new PatientDto("Test", "Male", date29, "M");
        patientMale30 = new PatientDto("Test", "Male", date30, "M");
        patientMale31 = new PatientDto("Test", "Male", date31, "M");
        patientFemale29 = new PatientDto("Test", "Male", date29, "F");
        patientFemale30 = new PatientDto("Test", "Male", date30, "F");
        patientFemale31 = new PatientDto("Test", "Male", date31, "F");
    }

    @BeforeEach
    public void setUpPerTest() {
        notesList = new ArrayList<>();
        notesList.clear();

        String[] triggerTerms = new String[] { "Hemoglobine A1C",
                "Microalbumine", "Taille", "Poids", "Fumeur", "Anormal",
                "Cholesterol", "Vertige", "Rechute", "Reaction", "Anticorps" };
        reportService = new ReportServiceImpl(microservicePatientProxy,
                microserviceNotesProxy, triggerTerms);

        notesList.add(new NoteDto("note 1"));
        notesList.add(new NoteDto("note 2"));
        notesList.add(new NoteDto("note 3"));
    }

    @Test
    @Tag("getDiabeteReport")
    @DisplayName("getDiabeteReport - Ok")
    public void givenTwentyNineYoMaleWithOneTerme_whenGetDiabeteReport_thenReturnReport()
            throws PatientException {
        // GIVEN
        patientMale29.setId(1L);

        notesList.add(new NoteDto(term1));

        when(microservicePatientProxy.getPatientPersonalInformations(1L))
                .thenReturn(patientMale29);
        when(microserviceNotesProxy.getAllPatientsNoteDto(
                patientMale29.getLastName(), patientMale29.getFirstName()))
                        .thenReturn(notesList);

        // WHEN
        Report result = reportService.getDiabeteReport(1L);

        // THEN
        assertThat(result.getDiabeteAssessment()).isEqualTo(Assessment.None);
        assertThat(result.getPatAge()).isEqualTo(29);
        assertThat(result.getPatFirstName()).isEqualTo("Male");
        assertThat(result.getPatLastName()).isEqualTo("Test");
        assertThat(result.getPatId()).isEqualTo(1L);
    }

    @Test
    @Tag("getDiabeteReport")
    @DisplayName("getDiabeteReport - Ok - No notes")
    public void givenNoNotes_whenGetDiabeteReport_thenReturnReport()
            throws PatientException {
        // GIVEN
        patientMale29.setId(1L);

        when(microservicePatientProxy.getPatientPersonalInformations(1L))
                .thenReturn(patientMale29);
        when(microserviceNotesProxy.getAllPatientsNoteDto(
                patientMale29.getLastName(), patientMale29.getFirstName()))
                        .thenReturn(notesList);

        // WHEN
        Report result = reportService.getDiabeteReport(1L);

        // THEN
        assertThat(result.getDiabeteAssessment()).isEqualTo(Assessment.None);
        assertThat(result.getPatAge()).isEqualTo(29);
        assertThat(result.getPatFirstName()).isEqualTo("Male");
        assertThat(result.getPatLastName()).isEqualTo("Test");
        assertThat(result.getPatId()).isEqualTo(1L);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - OK - None - 0 terms - 31 yo")
    public void givenThirtyOneYoMaleWithZeroTerms_whenGetAssessment_thenReturnNone()
            throws PatientException {
        // GIVEN

        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale31,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.None);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - None - 0 terms - 29 yo")
    public void givenTwentyNineOneYoMaleWithZeroTerms_whenGetAssessment_thenReturnNone()
            throws PatientException {
        // GIVEN

        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.None);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - None - 2 terms - 29 yo - Male")
    public void givenTwentyNineOneYoMaleWithTwoTerms_whenGetAssessment_thenReturnNone()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.None);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - None - 3 terms - 29 yo - Female")
    public void givenTwentyNineYoFemaleWithThreeTerms_whenGetAssessment_thenReturnNone()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.None);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - Borderline - 2 terms - 31 yo")
    public void givenThirtyOneYoFemaleWithTwoTerms_whenGetAssessment_thenReturnBorderline()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale31,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.Borderline);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - Borderline - 2 terms - 30 yo")
    public void givenThirtyYoFemaleWithTwoTerms_whenGetAssessment_thenReturnBorderline()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale30,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.Borderline);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - InDanger - 4 terms - 29 yo - Female")
    public void givenTwentyNineYoFemaleWithFourTerms_whenGetAssessment_thenReturnInDanger()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.InDanger);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - InDanger - 5 terms - 29 yo - Female")
    public void givenTwentyNineYoFemaleWithFiveTerms_whenGetAssessment_thenReturnInDanger()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.InDanger);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - InDanger - 6 terms - 29 yo - Female")
    public void givenTwentyNineYoFemaleWithSixTerms_whenGetAssessment_thenReturnInDanger()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.InDanger);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - InDanger - 3 terms - 29 yo - Male")
    public void givenTwentyNineYoMaleWithThreeTerms_whenGetAssessment_thenReturnInDanger()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.InDanger);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - InDanger - 4 terms - 29 yo - Male")
    public void givenTwentyNineYoMaleWithFourTerms_whenGetAssessment_thenReturnInDanger()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.InDanger);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - InDanger - 6 terms - 31 yo - Male")
    public void givenThirtyOneYoMaleWithSixTerms_whenGetAssessment_thenReturnInDanger()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale31,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.InDanger);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - InDanger - 6 terms - 31 yo - Female")
    public void givenThirtyOneYoFemaleWithSixTerms_whenGetAssessment_thenReturnInDanger()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale31,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.InDanger);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - InDanger - 7 terms - 31 yo - Male")
    public void givenThirtyOneYoMaleWithSevenTerms_whenGetAssessment_thenReturnInDanger()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale31,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.InDanger);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - InDanger - 7 terms - 31 yo - Female")
    public void givenThirtyOneYoFemaleWithSevenTerms_whenGetAssessment_thenReturnInDanger()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale31,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.InDanger);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - InDanger - 6 terms - 30 yo - Female")
    public void givenThirtyYoFemaleWithSixTerms_whenGetAssessment_thenReturnInDanger()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale30,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.InDanger);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 5 terms - 29 yo - Male")
    public void givenTwentyNineMaleWithFiveTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 6 terms - 29 yo - Male")
    public void givenTwentyNineMaleWithSixTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 7 terms - 29 yo - Male")
    public void givenTwentyNineMaleWithSevenTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 8 terms - 29 yo - Male")
    public void givenTwentyNineMaleWithHeightTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        notesList.add(new NoteDto(term8));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 9 terms - 29 yo - Male")
    public void givenTwentyNineMaleWithNineTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        notesList.add(new NoteDto(term8));
        notesList.add(new NoteDto(term9));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 7 terms - 29 yo - Female")
    public void givenTwentyNineFemaleWithSevenTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 8 terms - 29 yo - Female")
    public void givenTwentyNineFemaleWithHeightTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        notesList.add(new NoteDto(term8));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 9 terms - 29 yo - Female")
    public void givenTwentyNineFemaleWithNineTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        notesList.add(new NoteDto(term8));
        notesList.add(new NoteDto(term9));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale29,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 8 terms - 31 yo - Female")
    public void givenThirtyOneFemaleWithHeightTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        notesList.add(new NoteDto(term8));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale31,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 9 terms - 31 yo - Female")
    public void givenThirtyOneFemaleWithNineTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        notesList.add(new NoteDto(term8));
        notesList.add(new NoteDto(term9));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientFemale31,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 8 terms - 31 yo - Male")
    public void givenThirtyOneMaleWithHeightTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        notesList.add(new NoteDto(term8));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale31,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 9 terms - 31 yo - Male")
    public void givenThirtyOneMaleWithNineTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        notesList.add(new NoteDto(term8));
        notesList.add(new NoteDto(term9));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale31,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getDiabeteAssessment")
    @DisplayName("getDiabeteAssessment - EarlyOnset - 8 terms - 30 yo - Male")
    public void givenThirtyMaleWithHeightTerms_whenGetAssessment_thenReturnEarlyOnset()
            throws PatientException {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));
        notesList.add(new NoteDto(term4));
        notesList.add(new NoteDto(term5));
        notesList.add(new NoteDto(term6));
        notesList.add(new NoteDto(term7));
        notesList.add(new NoteDto(term8));
        // WHEN
        Assessment result = reportService.getDiabeteAssessment(patientMale30,
                notesList);

        // THEN
        assertThat(result).isEqualTo(Assessment.EarlyOnset);
    }

    @Test
    @Tag("getTriggerTermsNumber")
    @DisplayName("getTriggerTermsNumber - OK - 3 trigger terms")
    public void givenThreeTriggerTerms_whenGet_thenReturnThreeSize() {
        // GIVEN
        notesList.add(new NoteDto(term1));
        notesList.add(new NoteDto(term2));
        notesList.add(new NoteDto(term3));

        // WHEN
        long result = reportService.getTriggerTermsNumber(notesList);

        // THEN
        assertThat(result).isEqualTo(3);
    }

    @Test
    @Tag("getTriggerTermsNumber")
    @DisplayName("getTriggerTermsNumber - OK - 3 trigger terms - 1 with accent 'é'")
    public void givenThreeTriggerTerms_whenGetWithAccent_thenReturnThreeSize() {
        // GIVEN
        notesList.add(new NoteDto("Microalbumine"));
        notesList.add(new NoteDto("Hémoglobine A1C"));// accent
        notesList.add(new NoteDto("Poids"));

        // WHEN
        long result = reportService.getTriggerTermsNumber(notesList);

        // THEN
        assertThat(result).isEqualTo(3);
    }

    @Test
    @Tag("getTriggerTermsNumber")
    @DisplayName("getTriggerTermsNumber - OK - 3 trigger terms - 1 with accent 'â'")
    public void givenThreeTriggerTerms_whenGetWithAccentCriconflex_thenReturnThreeSize() {
        // GIVEN
        notesList.add(new NoteDto("Microâlbumine"));// accent
        notesList.add(new NoteDto("Hemoglobine A1C"));
        notesList.add(new NoteDto("Poids"));

        // WHEN
        long result = reportService.getTriggerTermsNumber(notesList);

        // THEN
        assertThat(result).isEqualTo(3);
    }

    @Test
    @Tag("getTriggerTermsNumber")
    @DisplayName("getTriggerTermsNumber - OK - 0 trigger terms ")
    public void givenThreeSizeNotes_whenGetWithoutTerms_thenReturnZeroSizeList() {
        // GIVEN

        // WHEN
        long result = reportService.getTriggerTermsNumber(notesList);

        // THEN
        assertThat(result).isEqualTo(0);
    }

    @Test
    @Tag("getTriggerTermsNumber")
    @DisplayName("getTriggerTermsNumber - OK - 6 trigger terms - 3 identicals")
    public void givenSixTriggerTerms_whenGetWithThreeIdenticals_thenReturnThreeSize() {
        // GIVEN
        notesList.add(new NoteDto("Microalbumine"));
        notesList.add(new NoteDto("Microâlbumine"));
        notesList.add(new NoteDto("Hemoglobine A1C"));
        notesList.add(new NoteDto("Hemoglobine A1C"));
        notesList.add(new NoteDto("Poids"));
        notesList.add(new NoteDto("Poids"));

        // WHEN
        long result = reportService.getTriggerTermsNumber(notesList);

        // THEN
        assertThat(result).isEqualTo(3);
    }

    @Test
    @Tag("isPatientsMale")
    @DisplayName("isPatientsMale - OK - Male")
    public void givenPatientsMale_whenCheckIfMale_thenReturnTrue()
            throws PatientException {
        // GIVEN
        // WHEN
        boolean result = reportService.isPatientsMale(patientGeneric1.getSex());

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    @Tag("isPatientsMale")
    @DisplayName("isPatientsMale - OK - Female")
    public void givenPatientsFemale_whenCheckIfMale_thenReturnFalse()
            throws PatientException {
        // GIVEN
        // WHEN
        boolean result = reportService.isPatientsMale(patientGeneric2.getSex());

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    @Tag("isPatientsMale")
    @DisplayName("isPatientsMale - Error - Empty sex")
    public void givenPatientWithEmptySex_whenCheck_thenReturnException()
            throws PatientException {

        assertThatExceptionOfType(PatientException.class).isThrownBy(() -> {
            reportService.isPatientsMale("");
        });
    }

    @Test
    @Tag("getAllPatientsNoteDto")
    @DisplayName("getAllPatientsNoteDto - OK - 3 notes")
    public void givenPatientWithNotes_whenGet_thenReturnAllNotesList() {
        // GIVEN
        when(microserviceNotesProxy.getAllPatientsNoteDto(
                patientMale29.getLastName(), patientMale29.getFirstName()))
                        .thenReturn(notesList);

        // WHEN
        List<NoteDto> result = reportService.getAllPatientsNoteDto(
                patientMale29.getLastName(), patientMale29.getFirstName());

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getNote()).isEqualTo("note 1");
        assertThat(result.get(1).getNote()).isEqualTo("note 2");
        assertThat(result.get(2).getNote()).isEqualTo("note 3");
    }

    @Test
    @Tag("getAllPatientsNoteDto")
    @DisplayName("getAllPatientsNoteDto - OK - 0 notes")
    public void givenPatientWithoutNotes_whenGet_thenReturnZeroListSize() {
        // GIVEN
        when(microserviceNotesProxy.getAllPatientsNoteDto(
                patientMale29.getLastName(), patientMale29.getFirstName()))
                        .thenReturn(new ArrayList<>());

        // WHEN
        List<NoteDto> result = reportService.getAllPatientsNoteDto(
                patientMale29.getLastName(), patientMale29.getFirstName());

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @Tag("getPatientPersonalInformations")
    @DisplayName("getPatientPersonalInformations - OK")
    public void givenPatientDto_whenGetInfos_thenReturnNotNull() {
        // GIVEN
        when(microservicePatientProxy.getPatientPersonalInformations(1L))
                .thenReturn(patientGeneric1);

        // WHEN
        PatientDto result = reportService.getPatientPersonalInformations(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBirthdate()).isEqualTo("1991-01-01");
        assertThat(result.getLastName()).isEqualTo("Generic1");
        assertThat(result.getFirstName()).isEqualTo("Patient1");
        assertThat(result.getSex()).isEqualTo("M");
    }

    @Test
    @Tag("getPatientPersonalInformations")
    @DisplayName("getPatientPersonalInformations - OK")
    public void givenPatientDto_whenGetInfosWithBadPatientId_thenReturnNull() {
        // GIVEN
        when(microservicePatientProxy.getPatientPersonalInformations(111L))
                .thenReturn(null);

        // WHEN
        PatientDto result = reportService.getPatientPersonalInformations(111L);

        // THEN
        assertThat(result).isNull();
    }
}
