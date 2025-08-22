package PaoloBarahona_20240048.PaoloBarahona_20240048.Services;

import PaoloBarahona_20240048.PaoloBarahona_20240048.Entities.LibroEntities;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Exceptions.LibroNotFound;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Models.DTO.LibroDTO;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Repositories.LibroRepositories;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LibroService {
    @Autowired
    private LibroRepositories repo;

    public List<LibroDTO>getAllLibros(){
        List<LibroEntities> PageEntity = repo.findAll();
        return PageEntity.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public LibroDTO update(Long id, @Valid LibroDTO jsonDTO){
        if (jsonDTO == null){
            throw new IllegalArgumentException("El libro no puede ser nulo");
        }

        LibroEntities libroData = repo.findById(id).orElseThrow(()->
                new LibroNotFound("Libro no encontrado"));
        libroData.setId(jsonDTO.getId());
        libroData.setTitulo(jsonDTO.getTitulo());
        libroData.setIsbn(jsonDTO.getIsbn());
        libroData.setAño_publicacion(jsonDTO.getAño_publicacion());
        libroData.setGenero(jsonDTO.getGenero());
        libroData.setAutor_id(jsonDTO.getAutor_id());
        return convertirADTO(libroData);
    }

    public boolean delete (Long id){

    }
}
