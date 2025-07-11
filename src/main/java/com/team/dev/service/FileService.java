package com.team.dev.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.team.dev.dto.RecolteRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileService {

    String uploadImage(String path, MultipartFile file) throws IOException;
    String uploadImages(String path, Long id,MultipartFile file) throws IOException;

    InputStream getRessource(String path, String fileName) throws FileNotFoundException;

    void exportToCsv(HttpServletResponse response, List<RecolteRequest> datas, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;

    public <T> void exportToCsv1(List<T> objects) throws IOException;
    public <T> String exportToCsvAndGetFile(List<T> objects) throws IOException;
}
