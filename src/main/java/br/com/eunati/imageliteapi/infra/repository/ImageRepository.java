package br.com.eunati.imageliteapi.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eunati.imageliteapi.domain.entity.Image;

public interface ImageRepository extends JpaRepository<Image, String>{

}
