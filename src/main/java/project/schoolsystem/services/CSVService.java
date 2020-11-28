package project.schoolsystem.services;

import org.springframework.web.multipart.MultipartFile;


public interface CSVService {

    void uploadCSV(MultipartFile csvFile);

}
