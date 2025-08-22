package PaoloBarahona_20240048.PaoloBarahona_20240048.Models.ApiResponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class ApiResponse <T>{
    private boolean succes;
    private String message;
    private T data;
    private String timestap;

    public ApiResponse(boolean succes, String message, T data){
        this.succes = succes;
        this.message = message;
        this.data = data;
        this.timestap = LocalDateTime.now().toString();
    }

    //Metodos estaticos para errores comunes
    public static <T> ApiResponse <T> succes(T data)
    {
        return new ApiResponse<>(true , "Operacion exitosa", data);
    }

    public static <T> ApiResponse <T> succes(String message, T data)
    {
        return new ApiResponse<>(true, message, data);
    }

    public static ApiResponse <?> error (String message){
        return new ApiResponse<>(false, message, null);
    }
}
