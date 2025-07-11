package com.team.dev.service.impl;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.team.dev.dto.RecolteRequest;
import com.team.dev.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        String name = file.getOriginalFilename();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MMdd_HHmmss")) ;

        String filename = timestamp + ".jpg";  // ou le format d'image approprié


        //random name generate file
        String randomID = UUID.randomUUID().toString();

        /*String fileName1 = null;
        if (name != null) {
            fileName1 = date; //randomID.concat(name.substring(name.lastIndexOf(".")));
        }else {
            throw new RuntimeException("Veuillez choisir un fichier !");
        }*/

        //full path
        String filePath = path + File.separator + filename;

        //creat folder is not created
        File f = new File(path);
        if(!f.exists()) {
            f.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));


        return filename;
    }

    @Override
    public String uploadImages(String path, Long id, MultipartFile file) throws IOException {

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MMdd_HHmmss")) ;

        String filename = timestamp +"_"+ id + ".jpg";  // ou le format d'image approprié


        //random name generate file
        String randomID = UUID.randomUUID().toString();

        /*String fileName1 = null;
        if (name != null) {
            fileName1 = date; //randomID.concat(name.substring(name.lastIndexOf(".")));
        }else {
            throw new RuntimeException("Veuillez choisir un fichier !");
        }*/

        //full path
        String filePath = path + File.separator + filename;

        //creat folder is not created
        File f = new File(path);
        if(!f.exists()) {
            f.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));


        return filename;

    }

    @Override
    public InputStream getRessource(String path, String fileName) throws FileNotFoundException {
        try {
            String fullPath = path + File.separator + fileName;
            InputStream is = new FileInputStream(fullPath);
            return is;
        }catch (Exception e){
            throw new RuntimeException("Image non trouvée !");
        }
    }

    @Override
    public void exportToCsv(HttpServletResponse response, List<RecolteRequest> datas, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        String filename = System.getProperty("user.home") + "/Downloads/recolte-data.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\"" + filename + "\"");

        StatefulBeanToCsv<RecolteRequest> writer = new StatefulBeanToCsvBuilder<RecolteRequest>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(datas);
    }


    public <T> String exportToCsvAndGetFile(List<T> objects) throws IOException {
        // Déterminer le dossier Téléchargements en fonction du système d'exploitation
        String downloadsDirectory = getDownloadsDirectory();

        // Définir le chemin complet du fichier CSV à générer
        String filePath = Paths.get(downloadsDirectory, "export.csv").toString();

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            // Générer dynamiquement les en-têtes à partir des noms de champs de l'objet
            String[] headers = generateHeaders(objects.get(0)); // Utiliser le premier objet pour générer les en-têtes
            writer.writeNext(headers); // Écrire les en-têtes dans le fichier CSV

            // Écrire les données des objets
            for (T obj : objects) {
                // Utiliser la réflexion pour obtenir les valeurs des champs
                String[] data = getObjectData(obj);
                writer.writeNext(data); // Écrire les données de chaque objet
            }
        }

        return filePath; // Retourner le chemin du fichier généré
    }

    private <T> String[] generateHeaders(T obj) {
        Field[] fields = obj.getClass().getDeclaredFields();  // Utilisation de Field
        String[] headers = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            headers[i] = fields[i].getName(); // Extraire le nom du champ pour l'en-tête
        }

        return headers;
    }

    public <T> void exportToCsv1(List<T> objects) throws IOException {
        // Déterminer le dossier Téléchargements en fonction du système d'exploitation
        String downloadsDirectory = getDownloadsDirectory();

        // Définir le chemin complet du fichier CSV à générer
        String filePath = Paths.get(downloadsDirectory, "export.csv").toString();

        try (CSVWriter writer = new CSVWriter(
                new FileWriter(filePath),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            // Écrire les en-têtes de colonnes
            String[] headers = generateHeaders(objects.get(0));
            writer.writeNext(headers);

            // Écrire les données des objets
            for (T obj : objects) {
                // Utiliser la réflexion pour obtenir les valeurs des champs
                String[] data = getObjectData(obj);
                writer.writeNext(data);
            }
        }
    }


    private <T> String[] getObjectData(T obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        String[] data = new String[fields.length];


        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);  // Accéder aux champs privés
                data[i] = String.valueOf(fields[i].get(obj));  // Extraire la valeur du champ
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // Gérer l'erreur d'accès
        }

        return data;
    }

    private String getDownloadsDirectory() {
        String os = System.getProperty("os.name").toLowerCase();

        // Déterminer le chemin en fonction du système d'exploitation
        if (os.contains("win")) {
            return System.getProperty("user.home") + "\\Downloads"; // Pour Windows
        } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
            return System.getProperty("user.home") + "/Downloads"; // Pour macOS et Linux
        } else {
            // Si l'OS n'est pas reconnu, vous pouvez retourner un chemin par défaut ou lever une exception
            throw new RuntimeException("Système d'exploitation non supporté pour la gestion du dossier Téléchargements.");
        }
    }
}
