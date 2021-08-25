package ru.javaops.topjava2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Menu;

import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository  extends BaseRepository<Menu>{
    @EntityGraph(attributePaths = {"restaurant","dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.forDate=current_date")
    List<Menu> getToday();
}
