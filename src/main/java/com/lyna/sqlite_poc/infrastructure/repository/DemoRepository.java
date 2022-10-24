package com.lyna.sqlite_poc.infrastructure.repository;

import com.lyna.sqlite_poc.infrastructure.DemoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemoRepository extends JpaRepository<DemoModel, Integer> {

    boolean existsByName(String name);

    List<DemoModel> findByName(String name);

    @Query("select max(m.id) from DemoModel m")
    Integer findMaxId();
}
