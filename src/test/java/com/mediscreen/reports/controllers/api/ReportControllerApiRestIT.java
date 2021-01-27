package com.mediscreen.reports.controllers.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.reports.domain.dto.NoteDto;
import com.mediscreen.reports.domain.dto.PatientDto;
import com.mediscreen.reports.services.ReportService;

@WebMvcTest(value = ReportControllerApiRest.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReportControllerApiRestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URI_GET_PATIENT_INFOS = "/api/reports/getPatientPersonalInformations";
    private static final String URI_GET_PATIENT_NOTES = "/api/reports/getAllPatientsNoteDto";
    private static final String URI_GET_REPORT = "/api/reports/report";

    @Test
    @Tag("/report")
    @DisplayName("GET report - OK - 200 - Existing patId")
    public void aaaa() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI_GET_REPORT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("patId", "1"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("/getAllPatientsNoteDto")
    @DisplayName("GET PatientPersonalInformations - OK - 200 - Existing patId")
    public void givenTwoNotes_whenGetNotes_thenReturnOk() throws Exception {
        List<NoteDto> allNotesList = new ArrayList<>();
        NoteDto note1 = new NoteDto("note 1");
        NoteDto note2 = new NoteDto("note 2");
        allNotesList.add(note1);
        allNotesList.add(note2);

        when(reportService.getAllPatientsNoteDto(1L)).thenReturn(allNotesList);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI_GET_PATIENT_NOTES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("patId", "1"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("/getAllPatientsNoteDto")
    @DisplayName("GET PatientPersonalInformations - OK - 200 - Empty list")
    public void givenZeroNotes_whenGetNotes_thenReturnOk() throws Exception {
        when(reportService.getAllPatientsNoteDto(1L))
                .thenReturn(new ArrayList<>());

        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI_GET_PATIENT_NOTES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("patId", "1"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("/getPatientPersonalInformations")
    @DisplayName("GET PatientPersonalInformations - OK - 200 - Existing patId")
    public void givenExistingPatientId_whenGetInfos_thenReturnOk()
            throws Exception {
        PatientDto patient = new PatientDto(1L, "Generic1", "Patient1",
                "1990-12-31", "M");
        when(reportService.getPatientPersonalInformations(1L))
                .thenReturn(patient);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI_GET_PATIENT_INFOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("patId", "1"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("/getPatientPersonalInformations")
    @DisplayName("GET PatientPersonalInformations - ERROR - 404 - Non-existant patId")
    public void givenNonExistantPatientId_whenGetInfos_thenReturnNotFound()
            throws Exception {
        when(reportService.getPatientPersonalInformations(1L)).thenReturn(null);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI_GET_PATIENT_INFOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("patId", "111"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound()).andReturn();
    }
}
