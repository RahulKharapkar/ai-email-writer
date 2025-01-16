package com.rk.ai_email_writer;

import lombok.Data;

@Data
public class EmailRequest {
    private String emailContent;
    private String tone;
}
