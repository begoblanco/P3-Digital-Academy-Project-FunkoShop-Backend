package org.factoriaf5.digital_academy.funko_shop.firebase;

import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private Storage storage;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadFailure() {
        when(multipartFile.getOriginalFilename()).thenReturn("image.png");

        try {
            when(multipartFile.getBytes()).thenThrow(new IOException("File conversion failed"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = imageService.upload(multipartFile);

        assertEquals("Image couldn't upload, Something went wrong", result);
    }
}
