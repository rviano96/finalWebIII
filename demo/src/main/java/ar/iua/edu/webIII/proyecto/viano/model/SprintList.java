package ar.iua.edu.webIII.proyecto.viano.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="sprint_lists",uniqueConstraints=
@UniqueConstraint(columnNames={"sprint_id", "name"}))
public class SprintList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id")
    private Integer id;

    @Column(name = "name")
    private String name;


    @ManyToOne()
    private Sprint sprint;



    @OneToMany(mappedBy = "listName",  cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Task> task;

    public SprintList() {
    }

    public SprintList(String name, Sprint sprint, List<Task> task) {
        super();
        this.name = name;
        this.sprint = sprint;
        this.task = task;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public List<Task> getTask() {
        return task;
    }

    public void setTask(List<Task> task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "SprintList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sprint='" + sprint + '\'' +
                ", task=" + task +
                '}';
    }
}
