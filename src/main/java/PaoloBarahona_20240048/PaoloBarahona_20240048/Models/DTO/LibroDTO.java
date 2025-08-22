package PaoloBarahona_20240048.PaoloBarahona_20240048.Models.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString @EqualsAndHashCode
public class LibroDTO {
    private Long id;

    @NotBlank(message = "EL titulo no puede ser nulo")
    private String titulo;

    @NotBlank(message = "EL isbn no puede ser nulo")
    private String isbn;

    @NotNull
    private int año_publicacion;

    @NotNull
    private String genero;

    @NotNull
    private int autor_id;
}
