package com.jumblar.core.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PointsReferenceSerializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(PointsReference.class, new PointsReferenceJSONSerializer());
        module.addDeserializer(PointsReference.class, new PointsReferenceJSONDeserializer());
        mapper.registerModule(module);
    }

    public static String serialise(PointsReference pointsReference){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gos = new GZIPOutputStream(outputStream)){

            gos.write(mapper.writeValueAsBytes(pointsReference));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public static PointsReference deserialize(String base64){
        byte[] decoded = Base64.getDecoder().decode(base64);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decoded);
        try (GZIPInputStream gis = new GZIPInputStream(inputStream)){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            return mapper.readValue(baos.toByteArray(), PointsReference.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
