package br.com.eunati.imageliteapi.domain.enums;

import org.springframework.http.MediaType;
import java.util.Arrays;

public enum ImageExtension {
    PNG(MediaType.IMAGE_PNG),
    GIF(MediaType.IMAGE_GIF),
    JPEG(MediaType.IMAGE_JPEG);

    private MediaType mediaType;

    ImageExtension(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public static ImageExtension valueOf(MediaType mediaType) {
        for (ImageExtension extension : values()) {
            if (extension.getMediaType().equals(mediaType)) {
                return extension;
            }
        }
        throw new IllegalArgumentException("Unsupported media type: " + mediaType);
    }

    public static ImageExtension ofName(String name) {
        return Arrays.stream(values())
            .filter(ie -> ie.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
}
