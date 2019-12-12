package ar.iua.edu.webIII.proyecto.viano.model;

import ar.iua.edu.webIII.proyecto.viano.web.Constantes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task")
public class Task {
    //private final static String LOW_PRIORITY = Constantes.LOW_PRIORITY;
    //private final static String MEDIUM_PRIORITY = Constantes.LOW_PRIORITY;
    //private final static String HIGH_PRIORITY = Constantes.LOW_PRIORITY;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    private Integer id;

    // el nombre de la terea no puede ser false
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private Date creation;
    @Column(name = "modification_date")
    private Date modification;
    @Column(name = "priority")
    private int priority;
    @Column(name = "estimation")
    private Integer estimation;

    @ManyToOne
    @JoinColumn(name = "list_id")
    @JsonIgnoreProperties("task")
    @JsonBackReference
    private SprintList listName;

    public Task() {
    }

    public Task(String name, Date creation, Date modification, String priority, Integer estimation, SprintList listName, String description) {
        this.name = name;
        this.creation = creation;
        this.modification = modification;
        setPriority(priority);
        this.estimation = estimation;
        this.listName = listName;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Date getModification() {
        return modification;
    }

    public void setModification(Date modification) {
        this.modification = modification;
    }

    public String getPriority() {
        switch (this.priority) {
            case 1:
                return Constantes.LOW_PRIORITY;
            case 2:
                return Constantes.MEDIUM_PRIORITY;
            case 3:
                return Constantes.HIGH_PRIORITY;
            default:
                return null;
        }
    }

    public void setPriority(String priority) {
        priority = priority.toLowerCase().trim();
        switch (priority) {
            case Constantes.LOW_PRIORITY:
                this.priority = 1;
                break;
            case Constantes.MEDIUM_PRIORITY:
                this.priority = 2;
                break;
            case Constantes.HIGH_PRIORITY:
                this.priority = 3;
                break;
            default:
                this.priority = 0;
                break;
        }

    }

    public Integer getEstimation() {
        return estimation;
    }

    public void setEstimation(Integer estimation) {
        this.estimation = estimation;
    }

    public SprintList getListName() {
        return listName;
    }

    public void setListName(SprintList listName) {
        this.listName = listName;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creation=" + creation +
                ", modification=" + modification +
                ", priority='" + priority + '\'' +
                ", estimation=" + estimation +
                ", listName=" + listName +
                ", description=" + description +
                '}';
    }
}
