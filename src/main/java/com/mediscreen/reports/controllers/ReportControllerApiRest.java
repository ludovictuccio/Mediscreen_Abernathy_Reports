package com.mediscreen.reports.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.reports.domain.dto.NoteDto;
import com.mediscreen.reports.domain.dto.PatientDto;
import com.mediscreen.reports.services.ReportService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/reports")
public class ReportControllerApiRest {

    private static final Logger LOGGER = LogManager
            .getLogger("ReportControllerApiRest");

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "GET patient's personal informations DTO", notes = "Need param patId (the patient's id) - Return response 200 OK or 404 not found")
    @GetMapping("/getPatientPersonalInformations")
    public ResponseEntity<PatientDto> getPatientPersonalInformations(
            @RequestParam final Long patId) {
        PatientDto patientDto = reportService
                .getPatientPersonalInformations(patId);

        if (patientDto != null) {
            LOGGER.info(
                    "GET request SUCCESS for: /api/reports/getPatientPersonalInformations");
            return new ResponseEntity<PatientDto>(patientDto, HttpStatus.OK);
        }
        LOGGER.info(
                "GET request FAILED for: /api/reports/getPatientPersonalInformations");
        return new ResponseEntity<PatientDto>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "GET all patient's notes DTO list", notes = "Need param patId (the patient's id) - Return response 200 OK or 404 not found")
    @GetMapping("/getAllPatientsNoteDto")
    public List<NoteDto> getAllPatientsNoteDto(@RequestParam final Long patId) {
        return reportService.getAllPatientsNoteDto(patId);
    }
}
