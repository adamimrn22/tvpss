package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.TvpssStatus;
import com.beyondtech.tvpss.model.TvpssVersion;
import com.beyondtech.tvpss.repository.TvpssVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Transactional
public class SchoolVersionStatusService {

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/uploads/tvpss/logos/";

    @Autowired
    private TvpssVersionRepository tvpssVersionRepository;

    public SchoolVersionStatusService() {
        createUploadDirectory();
    }

    private void createUploadDirectory() {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public TvpssVersion submitTvpssVersion(TvpssVersion tvpssVersion, MultipartFile logo) throws IOException {
        if (logo != null && !logo.isEmpty()) {
            String fileName = handleFileUpload(logo);
            tvpssVersion.setLogoPath(fileName);
        }

        tvpssVersion.setTvpssCurrentStatus(TvpssStatus.PENDING_VALIDASI);
        return tvpssVersionRepository.save(tvpssVersion);
    }

    private String handleFileUpload(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        Path filePath = Paths.get(UPLOAD_DIR + newFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return newFileName;
    }
}