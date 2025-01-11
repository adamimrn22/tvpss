package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.School;
import com.beyondtech.tvpss.model.TvpssStatus;
import com.beyondtech.tvpss.model.TvpssVersion;
import com.beyondtech.tvpss.repository.TvpssVersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class SchoolVersionStatusService {

    private static final Logger logger = LoggerFactory.getLogger(SchoolVersionStatusService.class);

    private final String projectRoot = System.getProperty("user.dir");
    private final String UPLOAD_DIR;
    private static final String UPLOAD_URL_PATH = "/uploads/tvpss/logos/";

    @Autowired
    private TvpssVersionRepository tvpssVersionRepository;

    @Autowired
    private SchoolService schoolService;


    public SchoolVersionStatusService(SchoolService schoolService) {
        UPLOAD_DIR = projectRoot + File.separator + "uploads" + File.separator +
                "tvpss" + File.separator + "logos" + File.separator;

        this.schoolService = schoolService;
    }

    @PostConstruct
    private void init() {
        try {
            createUploadDirectory();
        } catch (Exception e) {
            System.out.println("Failed to create upload directory " + e);
        }
    }

    private void createUploadDirectory() {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            logger.info("Creating directory {}: {}", UPLOAD_DIR, created ? "success" : "failed");
        } else {
            logger.info("Directory already exists: {}", UPLOAD_DIR);
        }

        if (!directory.canWrite()) {
            logger.error("Directory is not writable: {}", UPLOAD_DIR);
        }
    }

    private String handleFileUpload(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension;
        Path filePath = Paths.get(UPLOAD_DIR, newFileName);

        logger.info("Attempting to save file to: {}", filePath.toString());
        System.out.println("Attempting to save file to: {} " + filePath.toString());
        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File saved successfully");
            System.out.println("Saved");
            return newFileName;
        } catch (IOException e) {
            logger.error("Failed to save file", e);
            System.out.println("FAILED");
            throw e;
        }
    }


    public TvpssVersion submitTvpssVersion(TvpssVersion tvpssVersion, MultipartFile logo) throws IOException {
        if (logo != null && !logo.isEmpty()) {
            try {
                String fileName = handleFileUpload(logo);
                tvpssVersion.setLogoPath(UPLOAD_URL_PATH + fileName);
                logger.info("File successfully uploaded: {}", fileName);
                System.out.println("File successfully uploaded: " + fileName);
            } catch (IOException e) {
                logger.error("Failed to upload file", e);
                throw e;
            }
        }

        tvpssVersion.setTvpssCurrentStatus(TvpssStatus.PENDING);

        return tvpssVersionRepository.saveOrUpdate(tvpssVersion);
    }

    public List<School> getAllSchoolsByDistrict(String district) {
        List<School> schools = schoolService.getSchoolsByDistrict(district);

        System.out.println("district" + district);
        System.out.println("all school" + schools);


        for (School school : schools) {
            TvpssVersion version = tvpssVersionRepository.findBySchoolCode(school.getCode());

            System.out.println("the school" + school);

            if (version != null) {
                school.setTvpssVersion(version.getTvpssVersion());
                school.setTvpssStatus(version.getTvpssCurrentStatus());
            }
        }

        return schools;
    }

    public List<School> getAllSchool() {
        List<School> schools = schoolService.getAllSchools();
        System.out.println("all school" + schools);


        for (School school : schools) {
            TvpssVersion version = tvpssVersionRepository.findBySchoolCode(school.getCode());

            System.out.println("the school" + school);

            if (version != null) {
                school.setTvpssVersion(version.getTvpssVersion());
                school.setTvpssStatus(version.getTvpssCurrentStatus());
            }
        }

        return schools;
    }

    public Map<String, Object> getSchoolVersionWithSchoolData(String schoolCode){
        School school = schoolService.getSchoolByCode(schoolCode);
        TvpssVersion version = tvpssVersionRepository.findBySchoolCode(schoolCode);

        Map<String, Object> map = new HashMap<>();
        map.put("school", school);
        map.put("version", version);

        return map;
    }





}