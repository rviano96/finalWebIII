package ar.iua.edu.webIII.proyecto.viano.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sprints")
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Date startDate;
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<SprintList> sprintLists;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<SprintList> getSprintLists() {
        return sprintLists;
    }

    public void setSprintLists(List<SprintList> sprintLists) {
        this.sprintLists = sprintLists;
    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }



}