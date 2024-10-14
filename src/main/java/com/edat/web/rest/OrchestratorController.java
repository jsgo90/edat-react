package com.edat.web.rest;

import com.edat.service.dto.HistorialDTO;
import com.edat.web.rest.dto.YoloResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/orchestrator")
public class OrchestratorController {

    private final Logger log = LoggerFactory.getLogger(OrchestratorController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HistorialResource historialResource;

    @PostMapping("/processRequest")
    public ResponseEntity<String> processRequest(
        @RequestParam("image_dni") MultipartFile dni,
        @RequestParam("image_rostro") MultipartFile rostro
    ) {
        if (dni == null || dni.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La imagen del DNI es obligatoria.");
        }

        if (rostro == null || rostro.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La imagen del rostro es obligatoria.");
        }

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        try {
            builder.part("image_1", dni.getBytes()).filename("dni.jpg").contentType(MediaType.IMAGE_JPEG);

            builder.part("image_2", rostro.getBytes()).filename("rostro.jpg").contentType(MediaType.IMAGE_JPEG);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
        }

        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity = new HttpEntity<>(multipartBody, headers);
        String urlYolo = "http://localhost:9090/process_dni";

        ResponseEntity<String> responseYolo = restTemplate.postForEntity(urlYolo, requestEntity, String.class);

        if (!responseYolo.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(responseYolo.getStatusCode()).body("Error al extraer datos");
        }

        String responseBody = responseYolo.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            YoloResponse yoloResponse = objectMapper.readValue(responseBody, YoloResponse.class);

            double distance = yoloResponse.getDistance();
            boolean match = yoloResponse.isMatch();
            String message = yoloResponse.getMessage();
            String imageBase64 = yoloResponse.getImageBase64();

            if (yoloResponse.getOcrResults() != null) {
                String apellido = yoloResponse.getOcrResults().getApellido();
                String nombre = yoloResponse.getOcrResults().getNombre();
                String numeroDNI = yoloResponse.getOcrResults().getNumero_DNI();

                log.info("OCR Results - Apellido: {}, Nombre: {}, Numero DNI: {}", apellido, nombre, numeroDNI);
            }

            if (yoloResponse.isMatch()) {
                HistorialDTO historialDto = new HistorialDTO();
                historialDto.setAlumnoId(1L);
                historialDto.setAutorizadoId(10L);

                byte[] autorizadoRostroBytes = rostro.getBytes();
                historialDto.setAutorizadoRostro(autorizadoRostroBytes);

                if (imageBase64 != null && !imageBase64.isEmpty()) {
                    byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
                    historialDto.setAutorizadoDni(imageBytes);
                }

                historialResource.registrarSalida(historialDto);

                return ResponseEntity.ok("Las imágenes coinciden. Se ha verificado la misma persona.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Las imágenes no coinciden. No Autorizado.");
            }
        } catch (IOException e) {
            log.error("Error al procesar la respuesta JSON", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la respuesta de YOLO.");
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar registro de salida.");
        }
    }
}
