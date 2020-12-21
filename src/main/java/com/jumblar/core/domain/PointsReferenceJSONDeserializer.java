package com.jumblar.core.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class PointsReferenceJSONDeserializer extends StdDeserializer<PointsReference> {

    PointsReferenceJSONDeserializer(){
        this(null);
    }
    protected PointsReferenceJSONDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public PointsReference deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        byte[] salt = node.get("salt").binaryValue();
        byte[] vagueHash = node.get("vagueHash").binaryValue();
        int nPoints = node.get("nPoints").intValue();
        int N = node.get("N").intValue();
        int r = node.get("r").intValue();
        int p = node.get("p").intValue();
        int keyLength = node.get("keyLength").intValue();
        return new PointsReference(salt, vagueHash,
                new ScryptParams(N, r, p, keyLength),
                nPoints);
    }
}
