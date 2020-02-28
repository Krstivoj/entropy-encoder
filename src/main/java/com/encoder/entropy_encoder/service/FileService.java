package com.encoder.entropy_encoder.service;

import com.encoder.entropy_encoder.payload.MessagePayload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

@Component
public class FileService {

    public FileService(){
    }

    // make folder structuring like /files/username/file_name
    private void processFile(MultipartFile multipartFile, SimpMessageSendingOperations simpMessageSendingOperations)
            throws IOException {
        byte[] bytes = new byte[100*1024];
        int chunks = 0 ;
        String name = multipartFile.getOriginalFilename();
        OutputStream fileOutputStream = new FileOutputStream(new File("./" + name));
        InputStream stream = multipartFile.getInputStream();
        String percentage;
        while (chunks != multipartFile.getSize()) {
            chunks += stream.read(bytes);
            fileOutputStream.write(bytes);
            percentage = String.format("%.2f", 100.0*chunks * 1.0/multipartFile.getSize());
            MessagePayload messagePayload = new MessagePayload();
            messagePayload.setTitle(name);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", name);
            hashMap.put("percentage", percentage);
            messagePayload.setContent(hashMap);
            simpMessageSendingOperations.convertAndSend("/topic/reply",messagePayload);
        }

        stream.close();
        fileOutputStream.flush();
        fileOutputStream.close();

    }

    public void handleFileUpload(SimpMessageSendingOperations simpMessageSendingOperations, MultipartFile[] files)
            throws IOException {
        Iterator<MultipartFile> multipartFileIterator = Arrays.stream(files).iterator();
        while(multipartFileIterator.hasNext()) {
            this.processFile(multipartFileIterator.next(), simpMessageSendingOperations);
        }
    }
}
