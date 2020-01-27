package com.encoder.entropy_ecnoder.controller;

import com.encoder.entropy_ecnoder.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/file")
public class FileUploadController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private FileService fileService;
    @PostMapping("/upload")
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile[] file) throws Exception {
        this.fileService.handleFileUpload(messagingTemplate, file);
        return new ResponseEntity<>("File is successfully saved.", HttpStatus.OK);
    }
}
