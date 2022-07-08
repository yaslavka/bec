package matrixbackend.repository;

import matrixbackend.entity.TypeMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatrixTypeRepository extends JpaRepository<TypeMatrix,Long> {


    @Query(value = "SELECT * FROM `type_matrix` ORDER BY `id` ;",nativeQuery = true)
    List<TypeMatrix> findAllAndSortById();
}
