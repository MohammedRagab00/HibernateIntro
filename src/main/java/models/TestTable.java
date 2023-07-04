package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "testTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestTable {

  @Id
  private Integer id;
  private Integer age;
  private String name;
}
