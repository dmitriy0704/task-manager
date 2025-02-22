package dev.folomkin.taskmanager.domain.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_seq")
    @SequenceGenerator(name = "task_id_seq", sequenceName = "task_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;


    @Column(name = "priority")
    private String priority;

    @Column(name = "comments")
    private String comments;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @Column(name = "executor")
    private String executor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

//    @PrePersist
//    protected  void setCreatedAt() {
//        if (this.createdAt == null) createdAt = new Date();
//        if (this.updatedAt == null) updatedAt = new Date();
//        if (this.id == null);
//        try {
//            id = Long.parseLong(UUID.randomUUID().toString());
//        } catch (NumberFormatException nfe ) {
//            nfe.printStackTrace();
//        }
//    }
//
//
//    @PreUpdate
//    protected  void setUpdatedAt() {
//        this.updatedAt = new Date();
//    }
//
//
//    @PreRemove
//    protected  void setCompletedAt() {
//        this.completedAt = new Date();
//    }
    public Task(Long id,
                String title,
                String description,
                Status status,
                String priority,
                String comments,
                String executor
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.comments = comments;
        this.executor = executor;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }


}