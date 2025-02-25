package dev.folomkin.taskmanager.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_seq")
    @SequenceGenerator(name = "task_id_seq", sequenceName = "task_id_seq", allocationSize = 1)
    private Long id;

    @Schema(description = "Заголовок задачи")
    @Column(name = "title")
    private String title;

    @Schema(description = "Описание задачи")
    @Column(name = "description")
    private String description;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @Schema(description = "Статус", example = "PENDING, IN_PROGRESS, FINISHED")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Schema(description = "Приоритет задачи", example = "LOW, MEDIUM, HIGH")
    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Schema(description = "Комментарии к задаче")
    @Column(name = "comments")
    private String comments;

//    @Column(name = "author")
//    private String author;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    @Schema(description = "Исполнитель задачи. Идентифицируется по email")
    @Column(name = "executor")
    private String executor;


    public Task(Long id,
                String title,
                String description,
                Date createDate,
                Date modifyDate,
                Status status,
                Priority priority,
                String comments,
                User author,
                String executor
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.status = status;
        this.priority = priority;
        this.comments = comments;
        this.author = author;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}