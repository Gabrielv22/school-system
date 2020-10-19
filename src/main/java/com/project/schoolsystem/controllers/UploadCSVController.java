package com.project.schoolsystem.controllers;

import com.project.schoolsystem.dtos.UserDto;
import com.project.schoolsystem.services.CSVService;
import com.project.schoolsystem.utils.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "/upload")
public class UploadCSVController {

    @Autowired
    private CSVService csvService;


    @PostMapping(value = "/csv")
    public ResponseEntity<Void> uploadCSVFile(@RequestParam MultipartFile csvFile) {
        if (CSVHelper.hasCSVFormat(csvFile)) {
            csvService.uploadCSV(csvFile);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }

}
