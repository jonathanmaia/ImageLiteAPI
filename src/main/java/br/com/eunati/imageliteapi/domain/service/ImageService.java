package br.com.eunati.imageliteapi.domain.service;

import java.util.List;
import java.util.Optional;

import br.com.eunati.imageliteapi.domain.entity.Image;
import br.com.eunati.imageliteapi.domain.enums.ImageExtension;

public interface ImageService {
    Image save(Image image);
    Optional<Image> getById(String id);
    List<Image> search(ImageExtension extension, String query);
    void delete(String id);
}
