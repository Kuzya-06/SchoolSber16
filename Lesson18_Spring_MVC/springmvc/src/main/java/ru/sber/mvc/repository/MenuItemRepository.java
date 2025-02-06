package ru.sber.mvc.repository;

import ru.sber.mvc.model.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.processing.Generated;

@Generated(value = "excluded")
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Page<MenuItem> findAll(Pageable pageable);
}
