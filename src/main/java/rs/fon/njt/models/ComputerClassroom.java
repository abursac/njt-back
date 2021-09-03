package rs.fon.njt.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "computerclassrooms")
@PrimaryKeyJoinColumn(name = "id")
@Entity
public class ComputerClassroom extends Classroom{
}
