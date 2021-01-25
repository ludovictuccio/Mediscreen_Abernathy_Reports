package com.mediscreen.reports.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.reports.domain.dto.PatientDto;
import com.mediscreen.reports.proxies.MicroservicePatientProxy;
import com.mediscreen.reports.services.ReportService;

@RestController
@RequestMapping(value = "/api/reports")
public class ReportControllerApiRest {

    private static final Logger LOGGER = LogManager
            .getLogger("ReportControllerApiRest");

    @Autowired
    private ReportService reportService;

    @Autowired
    private MicroservicePatientProxy microservicePatientProxy;

    @GetMapping("/getPatientPersonalInformations")
    public PatientDto getPatientPersonalInformations(
            @RequestParam final Long patId) {
        return reportService.getPatientPersonalInformations(patId);
    }
}
