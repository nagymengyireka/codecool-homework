package com.codecool.homework.controller;

import com.codecool.homework.service.ExamTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
public class ExamTransferController {
    private ExamTransferService examTransferService;

    @Autowired
    public ExamTransferController(ExamTransferService examTransferService) {
        this.examTransferService = examTransferService;
    }

    @PostMapping()
    public void transferNewExams() {
        examTransferService.transferNewExams();
    }
}
