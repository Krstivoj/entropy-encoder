package com.encoder.entropy_ecnoder.controller;


import com.encoder.entropy_ecnoder.model.Arithmetic;
import com.encoder.entropy_ecnoder.model.Encoder;
import com.encoder.entropy_ecnoder.payload.ApiResponse;
import com.encoder.entropy_ecnoder.payload.EncoderRequest;
import com.encoder.entropy_ecnoder.repository.EncodeRepository;
import com.encoder.entropy_ecnoder.repository.UserRepository;
import com.encoder.entropy_ecnoder.security.CurrentUser;
import com.encoder.entropy_ecnoder.security.UserPrincipal;
import com.encoder.entropy_ecnoder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
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
    EncodeRepository encodeRepository;
    @Autowired
    private
    UserRepository userRepository;

    @PostMapping("/huffman")
    public ResponseEntity<?> huffmanEncode(@Valid @RequestBody EncoderRequest encoderRequest, @CurrentUser UserPrincipal currentUser){
        if (Utils.isEncoderRequestValid(encoderRequest) == 0) {
            Encoder encoder = Utils.generateHuffmanModel(userRepository.getOne(currentUser.getId()), encoderRequest);
            Encoder insertedEncoder = encodeRepository.save(encoder);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/huffman").build().toUri();

            return ResponseEntity.created(location).body(insertedEncoder);
        }else {
            return new ResponseEntity(new ApiResponse(false, "Sum of probabilities per symbol must be equal to 1.0"),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/arithmetic")
    public Arithmetic arithmeticEncode(@Valid @RequestBody EncoderRequest encoderRequest,@CurrentUser UserPrincipal currentUser){
        Arithmetic encode = Utils.generateArithmeticModel(userRepository.getOne(currentUser.getId()),encoderRequest);
        Arithmetic insertedEncode = encodeRepository.save(encode);
        return insertedEncode;
    }
    @GetMapping("/encoding/all")
    public Page<Encoder> getAllEncodings(Pageable pageable, @CurrentUser UserPrincipal  currentUser){
        return encodeRepository.getAllEncodingsForUser(currentUser.getId(),pageable);
    }
}
