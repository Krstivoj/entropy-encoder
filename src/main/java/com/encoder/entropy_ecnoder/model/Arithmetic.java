package com.encoder.entropy_ecnoder.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("arithmetic_encoder")
public class Arithmetic extends Encoder {

}
