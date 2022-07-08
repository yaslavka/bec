package matrixbackend.repository;

import com.querydsl.core.types.Predicate;
import matrixbackend.entity.Matrix;
import matrixbackend.entity.QMatrix;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatrixRepository extends JpaRepository<Matrix,Long>,
        QuerydslPredicateExecutor<Matrix>, QuerydslBinderCustomizer<QMatrix> {


    @Override
    default void customize(@NotNull QuerydslBindings bindings, @NotNull QMatrix matrix) {
    }

    @Override
    @EntityGraph(attributePaths = {"id","user.username","matrixEssenceEnum"})
    List<Matrix> findAll(Predicate value);




    @Query(value = "WITH RECURSIVE category_path (id, date,parent_id,user_id, matrix_essence,side_matrix) AS\n" +
            "(\n" +
            "  SELECT id, date,parent_id,user_id,matrix_essence,side_matrix" +
            "    FROM matrix" +
            "    WHERE parent_id =:parent_id" +
            "  UNION ALL" +
            " SELECT c.id, c.date,c.parent_id,c.user_id,c.matrix_essence,c.side_matrix" +
            "      FROM category_path AS cp JOIN matrix AS c" +
            "      ON cp.id = c.parent_id\n" +
            ")\n" +
            "SELECT * FROM category_path limit 7;",nativeQuery = true)
    LinkedList<Matrix> findByParentMatrixId(@Param("parent_id")Long id);

    Optional<Matrix> findById(Long id);



}
