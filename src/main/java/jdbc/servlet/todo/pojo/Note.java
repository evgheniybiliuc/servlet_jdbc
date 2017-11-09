package jdbc.servlet.todo.pojo;

import java.util.Date;

public class Note {
    private int id;
    private int user_id;
    private String title;
    private String description;
    private Status status;
    private Date createdAt;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Date createdAt) {
        this.createdAt = createdAt;
    }


    public enum Status {
        ACTIVE, DONE, REMOVED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (id != note.id) return false;
        if (user_id != note.user_id) return false;
        if (title != null ? !title.equals(note.title) : note.title != null) return false;
        if (description != null ? !description.equals(note.description) : note.description != null) return false;
        if (status != note.status) return false;
        if (createdAt != null ? !createdAt.equals(note.createdAt) : note.createdAt != null) return false;
        return user != null ? user.equals(note.user) : note.user == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + user_id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }
}
