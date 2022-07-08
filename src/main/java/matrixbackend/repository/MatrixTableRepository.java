package matrixbackend.repository;

import matrixbackend.entity.Matrix;
import matrixbackend.entity.MatrixTable;
import matrixbackend.entity.TypeMatrix;
import matrixbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface
MatrixTableRepository extends JpaRepository<MatrixTable,Long> {

    Optional <MatrixTable> findByUserAndTypeMatrixId(User user,Long matrix_type);

    Optional<MatrixTable> findByMatrixParent(Matrix matrixParent);

//    @Query(value = "SELECT * FROM matrix_table WHERE matrix_parent_id = :parent_id;",nativeQuery = true)
//    Optional<MatrixTable> getMatrixTableByParentId(Long parent_id);
}
