package com.rk.ai_email_writer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EmailGenController {

    private final EmailGenService emailGenService;

    public EmailGenController(EmailGenService emailGenService) {
        this.emailGenService = emailGenService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest) {
        String response = emailGenService.generateEmail(emailRequest);
        return ResponseEntity.ok(response);
    }

}
