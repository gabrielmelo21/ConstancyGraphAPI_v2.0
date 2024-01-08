package constacygraph.api.repository;

import constacygraph.api.models.Graphs;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GraphRepository extends JpaRepository<Graphs, Long> {
    List<Graphs> findAllByObjetivo(String objetivo, Sort sort);
}
