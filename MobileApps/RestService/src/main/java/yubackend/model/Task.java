package yubackend.model;

import javax.persistence.*;

@Entity(name="Task")
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer id;
    private String description;
    private int importance;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Version
    private Long version;

    public Task(){

    }

    public Task(String description,int importance){
        this.importance=importance;
        this.description=description;
    }

    public Task(int id,String description,int importance){
        this.id=id;
        this.importance=importance;
        this.description=description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }
}
