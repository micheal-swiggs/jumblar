package com.jumblar.core.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class PointsReferenceJSONSerializer extends StdSerializer<PointsReference> {

    PointsReferenceJSONSerializer(){
        this(null);
    }

    protected PointsReferenceJSONSerializer(Class<PointsReference> t) {
        super(PointsReference.class);
    }

    @Override
    public void serialize(PointsReference pointsReference, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeBinaryField("salt", pointsReference.salt);
        jsonGenerator.writeBinaryField("vagueHash", pointsReference.vagueHash);
        jsonGenerator.writeNumberField("nPoints", pointsReference.nPoints);
        jsonGenerator.writeNumberField("N", pointsReference.scryptParams.N);
        jsonGenerator.writeNumberField("r", pointsReference.scryptParams.r);
        jsonGenerator.writeNumberField("p", pointsReference.scryptParams.p);
        jsonGenerator.writeNumberField("keyLength", pointsReference.scryptParams.keyLength);
        jsonGenerator.writeEndObject();
    }
}
