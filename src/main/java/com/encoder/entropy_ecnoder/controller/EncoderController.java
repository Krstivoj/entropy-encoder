package com.encoder.entropy_ecnoder.controller;

import com.encoder.entropy_ecnoder.model.Encoder;
import com.encoder.entropy_ecnoder.payload.APIResponse;
import com.encoder.entropy_ecnoder.payload.EncoderDecoderRequest;
import com.encoder.entropy_ecnoder.repository.EncodeRepository;
import com.encoder.entropy_ecnoder.repository.UserRepository;
import com.encoder.entropy_ecnoder.security.CurrentUser;
import com.encoder.entropy_ecnoder.security.UserPrincipal;
import com.encoder.entropy_ecnoder.service.CodingService;
import com.encoder.entropy_ecnoder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/encoder")
public class EncoderController {

    @Autowired
    private
    UserRepository userRepository;
    @Autowired
    private Utils utils;
    @Autowired
    private CodingService codingService;
    @Autowired
    private EncodeRepository encodeRepository;

    @PostMapping("/huffman")
    public ResponseEntity<?> huffmanEncode(@Valid @RequestBody EncoderDecoderRequest encoderDecoderRequest,
                                           @CurrentUser UserPrincipal currentUser){
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/huffman").build().toUri();
        if (this.utils.validateRequest(encoderDecoderRequest) == 0) {
            return ResponseEntity
                    .created(location)
                    .body(codingService.encodeHuffman(currentUser, encoderDecoderRequest));
        }else {
            final String message = "Sum of probabilities per symbol must be equal to 1.0";
            return new ResponseEntity(
                    new APIResponse(false, message),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    @PostMapping("/arithmetic")
    public ResponseEntity<?> arithmeticEncode(@Valid @RequestBody EncoderDecoderRequest encoderDecoderRequest,
                                              @CurrentUser UserPrincipal currentUser){

        URI location = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/arithmetic")
                        .build()
                        .toUri();
        if (this.utils.validateRequest(encoderDecoderRequest) == 0) {
            return ResponseEntity
                    .created(location)
                    .body(codingService.encodeArithmetic(currentUser, encoderDecoderRequest));
        }
        else {
            final String message = "Sum of probabilities per symbol must be equal to 1.0";
            return new ResponseEntity(
                    new APIResponse(false, message),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    @GetMapping("/encoding/all")
    public Page<Encoder> getAllEncodings(Pageable pageable, @CurrentUser UserPrincipal  currentUser){
        return encodeRepository.getAllEncodingsForUser(currentUser.getId(),pageable);
    }
}
