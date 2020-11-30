package project.schoolsystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexControler {

    @GetMapping("/index")
    String getIndexPage() {
        return "index.html";
    }
}
