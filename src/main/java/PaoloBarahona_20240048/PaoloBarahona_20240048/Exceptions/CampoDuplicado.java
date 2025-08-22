package PaoloBarahona_20240048.PaoloBarahona_20240048.Exceptions;

import lombok.Getter;

public class CampoDuplicado extends RuntimeException {

    @Getter
    private String columnDuplicate;
    public CampoDuplicado(String message){
        super(message);
    }
    public CampoDuplicado(String message, String columnDuplicate){
        super(message);
        this.columnDuplicate = columnDuplicate;
    }
}
