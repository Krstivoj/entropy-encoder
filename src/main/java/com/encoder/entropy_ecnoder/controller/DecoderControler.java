package com.encoder.entropy_ecnoder.controller;

import com.encoder.entropy_ecnoder.payload.APIResponse;
import com.encoder.entropy_ecnoder.payload.EncoderDecoderRequest;
import com.encoder.entropy_ecnoder.security.CurrentUser;
import com.encoder.entropy_ecnoder.security.UserPrincipal;
import com.encoder.entropy_ecnoder.service.DecodingService;
import com.encoder.entropy_ecnoder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/decoder")

public class DecoderControler {

    @Autowired
    private
    Utils utils;
    @Autowired
    private
    DecodingService decodingService;

    @PostMapping("/arithmetic")
    public ResponseEntity<?> arithmeticDecode(@Valid @RequestBody EncoderDecoderRequest encoderDecoderRequest,
                                              @CurrentUser UserPrincipal currentUser){
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/arithmetic").build().toUri();
        if (this.utils.validateRequest(encoderDecoderRequest) == 0) {

            return ResponseEntity
                    .created(location)
                    .body(decodingService.decodeArithmetic(encoderDecoderRequest));
        }
        else {
            return new ResponseEntity(
                    new APIResponse(false, "Sum of probabilities per symbol must be equal to 1.0"),
                    HttpStatus.BAD_REQUEST
            );

        }
    }
}
