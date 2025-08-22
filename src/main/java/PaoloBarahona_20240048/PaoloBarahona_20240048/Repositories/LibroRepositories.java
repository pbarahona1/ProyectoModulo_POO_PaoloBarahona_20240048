package PaoloBarahona_20240048.PaoloBarahona_20240048.Repositories;

import PaoloBarahona_20240048.PaoloBarahona_20240048.Entities.LibroEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositories extends JpaRepository<LibroEntities, Long> {
}
