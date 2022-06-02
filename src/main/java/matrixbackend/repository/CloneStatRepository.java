package matrixbackend.repository;

import matrixbackend.entity.CloneStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloneStatRepository extends JpaRepository<CloneStat,Long> {
}
