package PaoloBarahona_20240048.PaoloBarahona_20240048.Services;

import PaoloBarahona_20240048.PaoloBarahona_20240048.Entities.LibroEntities;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Exceptions.LibroNotFound;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Models.DTO.LibroDTO;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Repositories.LibroRepositories;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
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

    public LibroDTO insert(@Valid LibroDTO jsonData){
        if (jsonData == null){
            throw new IllegalArgumentException("El libro no puede ser nulo");
        }
        try {
            LibroEntities libroEntities = convertirAEntity(jsonData);
            LibroEntities librosave = repo.save(libroEntities);
            return convertirADTO(librosave);
        }catch (Exception e){
            log.error("Error al registrar libro. " + e.getMessage());
            throw new LibroNotFound("Error al registrar el libro" + e.getMessage());
        }
    }



    public LibroDTO update(Long id, @Valid LibroDTO jsonDTO){
        if (jsonDTO == null){
            throw new IllegalArgumentException("El libro no puede ser nulo");
        }

        LibroEntities libroData = repo.findById(id).orElseThrow(()->
                new LibroNotFound("Libro no encontrado"));
        libroData.setTitulo(jsonDTO.getTitulo());
        libroData.setIsbn(jsonDTO.getIsbn());
        libroData.setAño_publicacion(jsonDTO.getAño_publicacion());
        libroData.setGenero(jsonDTO.getGenero());
        libroData.setAutor_id(jsonDTO.getAutor_id());
        return convertirADTO(libroData);
    }

    public boolean delete (Long id){
        try{
            LibroEntities objEntity = repo.findById(id).orElse(null);
            if (objEntity != null){
                repo.deleteById(id);
                return true;
            }
            return false;
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro ningun libro con ID: " + id , 1);
        }
    }

    private LibroDTO convertirADTO(LibroEntities objEntities){
        LibroDTO dto = new LibroDTO();
        dto.setId(objEntities.getId());
        dto.setTitulo(objEntities.getTitulo());
        dto.setIsbn(objEntities.getIsbn());
        dto.setAño_publicacion(objEntities.getAño_publicacion());
        dto.setGenero(objEntities.getGenero());
        dto.setAutor_id(objEntities.getAutor_id());
        return dto;
    }

    private LibroEntities convertirAEntity(@Valid LibroDTO json){
        LibroEntities objEntity = new LibroEntities();
        objEntity.setId(json.getId());
        objEntity.setTitulo(json.getTitulo());
        objEntity.setIsbn(json.getIsbn());
        objEntity.setAño_publicacion(json.getAño_publicacion());
        objEntity.setGenero(json.getGenero());
        objEntity.setAutor_id(json.getAutor_id());
        return objEntity;
    }

    public LibroEntities getLibroById(Long id) {
        return repo.findById(id).orElseThrow(()-> new LibroNotFound("El libro no fue encontrado con id: " + id + "no existe"));
    }

    public Page <LibroDTO> getAllLibros(int page, int size){
        Pageable pageable = QPageRequest.of(page, size);
        Page<LibroEntities>pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

}

