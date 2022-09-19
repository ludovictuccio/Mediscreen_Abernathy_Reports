package com.mediscreen.reports.proxies;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mediscreen.reports.domain.dto.NoteDto;

@FeignClient(value = "microservice-notes", url = "${proxy.notes}")
public interface MicroserviceNotesProxy {

    @GetMapping("/api/note/getAllPatientsNoteDto")
    List<NoteDto> getAllPatientsNoteDto(@RequestParam String lastName,
            @RequestParam String firstName);

}
