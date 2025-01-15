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
import java.util.*;

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

        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File saved successfully: {}", newFileName);
            return newFileName;
        } catch (IOException e) {
            logger.error("Failed to save file", e);
            throw e;
        }
    }


    public TvpssVersion submitTvpssVersion(TvpssVersion tvpssVersion, MultipartFile logo) throws IOException {
        // First, get the existing version if any
        TvpssVersion existingVersion = tvpssVersionRepository.findBySchoolCode(tvpssVersion.getSchoolCode());

        // If this is an update, preserve the existing logo path when no new file is uploaded
        if (existingVersion != null && (logo == null || logo.isEmpty())) {
            tvpssVersion.setLogoPath(existingVersion.getLogoPath());
            logger.info("Preserving existing logo path: {}", existingVersion.getLogoPath());
        }

        // Handle new file upload if provided
        if (logo != null && !logo.isEmpty()) {
            try {
                // Delete old file if it exists
                if (existingVersion != null && existingVersion.getLogoPath() != null) {
                    String oldFileName = existingVersion.getLogoPath().substring(
                            existingVersion.getLogoPath().lastIndexOf("/") + 1);
                    Path oldLogoPath = Paths.get(UPLOAD_DIR, oldFileName);
                    Files.deleteIfExists(oldLogoPath);
                    System.out.println("Old logo file deleted: {}" +  oldLogoPath);
                }

                // Upload the new logo
                String fileName = handleFileUpload(logo);
                tvpssVersion.setLogoPath(UPLOAD_URL_PATH + fileName);
                logger.info("New file successfully uploaded: {}", fileName);
                System.out.println("New file successfully uploaded: {}" +  fileName);

            } catch (IOException e) {
                logger.error("Failed to handle file upload", e);
                throw e;
            }
        }

        tvpssVersion.setTvpssCurrentStatus(TvpssStatus.PENDING);
        return tvpssVersionRepository.saveOrUpdate(tvpssVersion);
    }


    public List<Map<String, Object>> getAllSchoolsByDistrict(String district) {
        List<School> schools = schoolService.getSchoolsByDistrict(district);
        List<Map<String, Object>> schoolDataList = new ArrayList<>();

        for (School school : schools) {
            TvpssVersion version = tvpssVersionRepository.findBySchoolCode(school.getCode());
            Map<String, Object> map = new HashMap<>();
            map.put("school", school);
            map.put("version", version);
            System.out.println("vedrsi" + version);
            schoolDataList.add(map);
        }

        return schoolDataList;
    }

    public List<Map<String, Object>> getAllSchool() {
        List<School> schools = schoolService.getAllSchools();
        System.out.println("all school" + schools);
        List<Map<String, Object>> schoolDataList = new ArrayList<>();

        for (School school : schools) {
            TvpssVersion version = tvpssVersionRepository.findBySchoolCode(school.getCode());

            Map<String, Object> map = new HashMap<>();
            map.put("school", school);
            map.put("version", version);
            schoolDataList.add(map);
        }

        return schoolDataList;
    }

    public Map<String, Object> getSchoolVersionWithSchoolData(String schoolCode){
        School school = schoolService.getSchoolByCode(schoolCode);
        TvpssVersion version = tvpssVersionRepository.findBySchoolCode(schoolCode);

        Map<String, Object> map = new HashMap<>();
        map.put("school", school);
        map.put("version", version);

        return map;
    }

    public void updateTvpssVersion(String schoolCode, int version){
        tvpssVersionRepository.updateTvpssVersion(schoolCode, version, TvpssStatus.SUDAH);
    }

    public Integer getTvpssVersion(String schoolCode) {
        return tvpssVersionRepository.getTvpssVersion(schoolCode);
    }

    public Long countTvpssVersion(Long version){
        return tvpssVersionRepository.countTvpssVersions(version);
    }

    public Long countTvpssVersionsByDistrictAndVersion(String district, Long version) {
        return tvpssVersionRepository.countTvpssVersionsByDistrictAndVersion(district, version);
    }

    public Map<String, Long> countTvpssVersionsByVersion(int version) {
        return tvpssVersionRepository.countTvpssVersionsByVersion(version);
    }

    public Long countTvpssVersionByDistrictAndStatus(String district, TvpssStatus status) {
        return tvpssVersionRepository.countTvpssVersionByDistrictAndStatus(district, status);
    }
}