package com.mediscreen.reports.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mediscreen.reports.domain.dto.PatientDto;

@FeignClient(value = "microservice-patient", url = "${proxy.patient}")
public interface MicroservicePatientProxy {

    @GetMapping("/api/patient/getPatientPersonalInformations/{patId}")
    public PatientDto getPatientPersonalInformations(
            @PathVariable("patId") Long patId);

}
