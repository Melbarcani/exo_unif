package com.lyna.sqlite_poc.infrastructure;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class DemoModel {
    @Id
    private Integer id;
    private String name;
}
