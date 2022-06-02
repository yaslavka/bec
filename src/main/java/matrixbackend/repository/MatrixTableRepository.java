package matrixbackend.repository;

import matrixbackend.entity.Matrix;
import matrixbackend.entity.MatrixTable;
import matrixbackend.entity.TypeMatrix;
import matrixbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface
MatrixTableRepository extends JpaRepository<MatrixTable,Long> {

    Optional <MatrixTable> findByUserAndTypeMatrixId(User user,Long matrix_type);
}
