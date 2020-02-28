package com.encoder.entropy_encoder.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("arithmetic_encoder")
public class Arithmetic extends Encoder {

}
