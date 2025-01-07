package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.School;
import com.beyondtech.tvpss.model.SchoolNameAndCode;
import com.beyondtech.tvpss.response.SchoolApiResponse;
import com.beyondtech.tvpss.response.SingleSchoolApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolService {

    @Autowired
    private RestTemplate restTemplate;
    private final String apiUrl = "http://localhost:8083/api/schools";

    public List<School> getAllSchools() {
        SchoolApiResponse response = restTemplate.getForObject(apiUrl, SchoolApiResponse.class);
        if (response != null && response.isSuccess()) {
            return response.getData();
        }
        return List.of();
    }

    public List<SchoolNameAndCode> getSchoolNamesAndCodesByDistrict(String district) {
        String url = apiUrl + "/district/" + district;

        SchoolApiResponse response = restTemplate.getForObject(url, SchoolApiResponse.class);

        if (response != null && response.isSuccess()) {

            return response.getData().stream()
                    .map(school -> new SchoolNameAndCode(school.getName(), school.getCode()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public List<School> getSchoolsByDistrict(String district) {
        String url = apiUrl + "/district/" + district;
        SchoolApiResponse response = restTemplate.getForObject(url, SchoolApiResponse.class);

        if (response != null && response.isSuccess()) {
            return response.getData();
        }
        return List.of();
    }

    public School getSchoolByCode(String schoolCode) {
        String url = apiUrl + "/code/" + schoolCode;
        SingleSchoolApiResponse response = restTemplate.getForObject(url, SingleSchoolApiResponse.class);
        if (response != null && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }
}
