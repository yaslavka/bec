package matrixbackend.repository;

import matrixbackend.entity.TypeMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatrixTypeRepository extends JpaRepository<TypeMatrix,Long> {
}
