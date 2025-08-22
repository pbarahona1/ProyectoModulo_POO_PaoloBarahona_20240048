package PaoloBarahona_20240048.PaoloBarahona_20240048.Controller;

import PaoloBarahona_20240048.PaoloBarahona_20240048.Entities.LibroEntities;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Exceptions.CampoDuplicado;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Exceptions.LibroNotFound;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Models.ApiResponse.ApiResponse;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Models.DTO.LibroDTO;
import PaoloBarahona_20240048.PaoloBarahona_20240048.Services.LibroService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Api/Libros")
public class LibroController {

    //Injectamos la  clase service
    @Autowired
    private LibroService service;

    //Esto seria el endpoint para ir a llamar a la tabla y que se muestre
    @GetMapping("/getDataLibros")
    private ResponseEntity<List<LibroDTO>>getdata(){
        //Aqui le damos un nombre al metodo getAllLibros
        List<LibroDTO> libro = service.getAllLibros();
        //Y aqui hacemos la validacion
        if (libro == null){
            ResponseEntity.badRequest().body(Map.of(
                    "Status", "No hay libros registrados"
            ));
        }
        //si esta bien se regresa el nombre del metodo que creamos que en este caso seria libro
        return ResponseEntity.ok(libro);
    }

    @GetMapping("/getDataLibros/{id}")
    public ResponseEntity<ApiResponse<LibroEntities>>getLibroById(@PathVariable Long id){
        LibroEntities libroEntities = service.getLibroById(id);
        ApiResponse<LibroEntities> response = new ApiResponse<>(true, "Libro encontrado", libroEntities);
        return ResponseEntity.ok(response);
    }
    //Esto seria el endpoint para ir a agregar datos a la tabla
    @PostMapping("/newLibro")
    private ResponseEntity<Map<String, Object>>insertarLibro(
            @Valid @RequestBody LibroDTO json, HttpServletRequest request){
        try {
            //se le pone un nombre a la llamda del metodo insert que esta en el service
            LibroDTO response = service.insert(json);
            //Se hace la validacion de si el metodo que llamamos es nulo
            if (response == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "Error", "Inserccion incorreca",
                        "Estatus", "Inserccion incorrecta"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "Estado", "completado",
                    "data", response
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "Status", "Error",
                    "message", "Error al registrar libro",
                    "detail", e.getMessage()
            ));
        }
    }

    //Esto seria el endpoint para ir a editar un libro
    @PutMapping("/updateLibros/{id}")
    public ResponseEntity<?>modificarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody LibroDTO usuario,
            BindingResult bindingResult){
        //Hacemos un if para verificar si el bindigResult tiene o no errores
        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        //Si el binding esta bien se hace este try
        try {
            LibroDTO usuarioActualizado = service.update(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        }//Si el usuario no existe se pasa al catch y se muestra este error
        catch (LibroNotFound e){
            return ResponseEntity.notFound().build();
        }//Si hay un dato duplicado se manda el error
        catch (CampoDuplicado e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "datos duplicados", "campo", e.getColumnDuplicate())
            );
        }
    }

    @DeleteMapping("/deletelibros/{id}")
    public ResponseEntity<Map<String, Object>>eliminarlibro(@PathVariable Long id){
        try {
            if (!service.delete(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        //Aqui agregare un header perzonalizado
                        .header("x-Mensage-Error", "Libro no encontrado")
                        //Cuerpo del error
                        .body(Map.of(
                                "Error", "Not found", //Tipo de error
                                "message", "El libro no ha sido encontrado",//Mensaje descriptivo
                                "timestap", Instant.now().toString()//Marca de tiempo de error
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "Status", "Proceso completado",
                    "message", "Libro eliminado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "Status", "Error",
                    "message", "Error al eliminar categoria",
                    "detail", e.getMessage()
            ));
        }
    }
}
