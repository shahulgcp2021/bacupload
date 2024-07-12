package com.bacuti.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AwsParameterStoreService {

    private static final Logger log = LoggerFactory.getLogger(AwsParameterStoreService.class);
    private final SsmClient ssmClient;

    public AwsParameterStoreService(SsmClient ssmClient) {
        this.ssmClient = ssmClient;
    }

    public void putParameter(String key, String value) {
        try {
            // Retrieve the existing parameter value
            String existingValue = getParameter(key);

            // Combine existing value with the new value
            String combinedValue = combineValues(existingValue, value);

            // Update the parameter with the combined value
            PutParameterRequest parameterRequest = PutParameterRequest.builder()
                .name(key)
                .value(combinedValue)
                .type(ParameterType.STRING_LIST)
                .overwrite(true)
                .build();

            PutParameterResponse parameterResponse = ssmClient.putParameter(parameterRequest);
            log.info("Parameter stored with version: " + parameterResponse.version());

        } catch (SsmException e) {
            log.error("Error storing parameter in AWS Parameter Store", e);
            throw new RuntimeException("Error storing parameter in AWS Parameter Store", e);
        }
    }

    public String getParameter(String key) {
        try {
            GetParameterRequest getParameterRequest = GetParameterRequest.builder()
                .name(key)
                .withDecryption(true)
                .build();

            GetParameterResponse getParameterResponse = ssmClient.getParameter(getParameterRequest);
            return getParameterResponse.parameter().value();

        } catch (ParameterNotFoundException e) {
            log.warn("Parameter not found: " + key);
            return "";
        } catch (SsmException e) {
            log.error("Error retrieving parameter from AWS Parameter Store", e);
            throw new RuntimeException("Error retrieving parameter from AWS Parameter Store", e);
        }
    }

    private String combineValues(String existingValue, String newValue) {
        if (existingValue == null || existingValue.isEmpty()) {
            return newValue;
        }

        // Split the existing value into a list and add the new value
        List<String> values = new ArrayList<>(Arrays.asList(existingValue.split(",")));
        values.add(newValue);

        // Join the list into a comma-separated string
        return String.join(",", values);
    }
}
