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

import com.mediscreen.reports.domain.dto.NoteDto;
import com.mediscreen.reports.domain.dto.PatientDto;
import com.mediscreen.reports.exceptions.PatientException;
import com.mediscreen.reports.proxies.MicroserviceNotesProxy;
import com.mediscreen.reports.proxies.MicroservicePatientProxy;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    // @Autowired
    private ReportServiceImpl reportService;

    @Mock
    private MicroservicePatientProxy microservicePatientProxy;

    @Mock
    private MicroserviceNotesProxy microserviceNotesProxy;

    private List<NoteDto> notesList = new ArrayList<>();

    private static PatientDto patientGeneric1 = new PatientDto(1L, "Generic1",
            "Patient1", "1990-12-31", "M");
    private static PatientDto patientGeneric2 = new PatientDto(2L, "Generic2",
            "Patient2", "2000-01-15", "F");

    @BeforeEach
    public void setUpPerTest() {
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
    @Tag("getTriggerTermsNumber")
    @DisplayName("getTriggerTermsNumber - OK - 3 trigger terms")
    public void givenThreeTriggerTerms_whenGet_thenReturnThreeSize() {
        // GIVEN
        notesList.add(new NoteDto("Microalbumine"));
        notesList.add(new NoteDto("Hemoglobine A1C"));
        notesList.add(new NoteDto("Poids"));

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
        when(microserviceNotesProxy.getAllPatientsNoteDto(1L))
                .thenReturn(notesList);

        // WHEN
        List<NoteDto> result = reportService.getAllPatientsNoteDto(1L);

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
        when(microserviceNotesProxy.getAllPatientsNoteDto(1L))
                .thenReturn(new ArrayList<>());

        // WHEN
        List<NoteDto> result = reportService.getAllPatientsNoteDto(1L);

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
        assertThat(result.getBirthdate()).isEqualTo("1990-12-31");
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
