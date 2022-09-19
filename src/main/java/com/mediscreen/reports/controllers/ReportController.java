package com.mediscreen.reports.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mediscreen.reports.exceptions.PatientException;
import com.mediscreen.reports.services.ReportService;

import io.swagger.annotations.ApiOperation;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "GET patient's diabetes assessment report", notes = "THYMELEAF - Return response 200 or 404 not found with message's error 'Patient not found with this id'")
    @GetMapping("/report")
    public String report(@RequestParam("patId") final Long patId,
            final Model model) throws PatientException {
        model.addAttribute("report", reportService.getDiabeteReport(patId));
        return "report";
    }

    @ApiOperation(value = "GET patients list", notes = "THYMELEAF - Return to patient microservice - For search an other patient")
    @GetMapping("/patient/list")
    public ModelAndView returnAllPatientsList(final ModelMap model) {
        return new ModelAndView("redirect:http://localhost:8081/patient/list",
                model);
    }

}
