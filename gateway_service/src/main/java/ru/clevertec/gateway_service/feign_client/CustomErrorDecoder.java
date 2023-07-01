package ru.clevertec.gateway_service.feign_client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        StringBuilder responseBody = new StringBuilder();
        try {
            Reader reader = response.body().asReader(StandardCharsets.UTF_8);
            int symbol;
            while ((symbol = reader.read()) != -1) {
                responseBody.append((char) symbol);
            }
            String body = responseBody.toString().replaceAll("\\d", "");
            return new HttpClientErrorException(HttpStatusCode.valueOf(response.status()), body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  defaultErrorDecoder.decode(methodKey, response);
    }
}
