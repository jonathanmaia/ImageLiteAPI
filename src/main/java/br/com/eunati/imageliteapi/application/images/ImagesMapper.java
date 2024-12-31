package br.com.eunati.imageliteapi.application.images;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.eunati.imageliteapi.domain.entity.Image;
import br.com.eunati.imageliteapi.domain.enums.ImageExtension;

@Component
public class ImagesMapper {
    public Image mapToImage(MultipartFile file, String name, List<String> tags) throws IOException{
        return Image.builder()
            .name(name)
            .tags(String.join(",", tags))
            .size(file.getSize())
            .extension(ImageExtension.valueOf(MediaType.valueOf(file.getContentType())))
            .file(file.getBytes())
            .build();
    }

    public ImageDTO imageToDTO(Image image, String url) {
        return ImageDTO.builder()
            .url(url)
            .name(image.getName())
            .extension(image.getExtension().name())
            .size(image.getSize())
            .uploadDate(image.getUploadDate().toLocalDate())
            .build();
    }
}
