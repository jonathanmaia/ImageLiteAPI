package br.com.eunati.imageliteapi.application.images;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.eunati.imageliteapi.domain.entity.Image;
import br.com.eunati.imageliteapi.domain.enums.ImageExtension;
import br.com.eunati.imageliteapi.domain.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/v1/images")
@Slf4j
@RequiredArgsConstructor
public class ImagesController {

    private final ImageService service;
    private final ImagesMapper mapper;

    /**
     * Salva uma nova imagem.
     * 
     * @param file o arquivo da imagem
     * @param name o nome da imagem
     * @param tags as tags associadas à imagem
     * @return ResponseEntity com a URL da imagem criada
     * @throws IOException se ocorrer um erro ao processar o arquivo
     */
    @PostMapping
    public ResponseEntity save(
        @RequestParam("file") MultipartFile file,
        @RequestParam("name") String name,
        @RequestParam("tags") List<String> tags
    ) throws IOException {
        log.info("Imagem recebida: name: {}, size: {}", file.getOriginalFilename(), file.getSize());

        Image image = mapper.mapToImage(file, name, tags);
        Image savedImage = service.save(image);
        URI imageURL = buildImageURL(savedImage);

        return ResponseEntity.created(imageURL).build();
    }

    /**
     * Obtém uma imagem pelo seu ID.
     * 
     * @param id o ID da imagem
     * @return ResponseEntity com os bytes da imagem ou 404 se não encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]>  getImage(@PathVariable String id) {
        var possibleImage = service.getById(id);
        if(possibleImage.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var image = possibleImage.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(image.getExtension().getMediaType());
        headers.setContentLength(image.getSize());
        headers.setContentDispositionFormData("inline; filename=\"" + image.getFileName() + "\"", image.getFileName());

        return new ResponseEntity(image.getFile(), headers, HttpStatus.OK);

    }

    /**
     * Pesquisa imagens com base na extensão e consulta fornecidas.
     * 
     * @param extension a extensão da imagem (opcional)
     * @param query a consulta de pesquisa (opcional)
     * @return ResponseEntity com a lista de imagens encontradas
     */
    @GetMapping("/")
    public ResponseEntity<List<ImageDTO>> search(
        @RequestParam(value = "extension", required = false, defaultValue = "") String extension, 
        @RequestParam(value = "query", required = false, defaultValue = "") String query) {
        
        var result = service.search(ImageExtension.ofName(extension), query);
        var images = result.stream().map(image -> {
            var url = buildImageURL(image);
            return mapper.imageToDTO(image, url.toString());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(images);
    }

    /**
     * Constrói a URL da imagem.
     * 
     * @param image a imagem
     * @return a URI da imagem
     */
    private URI buildImageURL(Image image){
        String imagePath = "/" + image.getId();
        return ServletUriComponentsBuilder.fromCurrentRequestUri().path(imagePath).build().toUri();
    }

    /**
     * Deleta uma imagem pelo seu ID.
     * 
     * @param id o ID da imagem
     * @return ResponseEntity sem conteúdo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable String id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
