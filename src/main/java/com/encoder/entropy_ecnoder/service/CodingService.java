package com.encoder.entropy_ecnoder.service;

import com.encoder.entropy_ecnoder.model.Arithmetic;
import com.encoder.entropy_ecnoder.model.Encoder;
import com.encoder.entropy_ecnoder.payload.EncoderDecoderRequest;
import com.encoder.entropy_ecnoder.repository.EncodeRepository;
import com.encoder.entropy_ecnoder.security.UserPrincipal;
import com.encoder.entropy_ecnoder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodingService {
    @Autowired
    private Utils utils;
    @Autowired
    private
    EncodeRepository encodeRepository;

    public CodingService(){
    }

    public Encoder encodeHuffman(UserPrincipal currentUser, EncoderDecoderRequest encoderDecoderRequest){
        Encoder encoder = this.utils.generateHuffmanModel(UserPrincipal.getUser(currentUser), encoderDecoderRequest);
        return this.encodeRepository.save(encoder);
    }
    public Arithmetic encodeArithmetic(UserPrincipal currentUser, EncoderDecoderRequest encoderDecoderRequest){
        Arithmetic encode = this.utils.generateArithmeticModel(UserPrincipal.getUser(currentUser), encoderDecoderRequest);
        return encodeRepository.save(encode);
    }
}
