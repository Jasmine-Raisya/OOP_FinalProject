

//This is the task class: this is used to set the features of the tasks to show their description task name and to complete

public class Task  {

    private String description;
    private boolean completed;
    private Long completionTime;


    public Task(String description) {
        this.description = description;
        this.completed = false;

    }



    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Long completionTime) {
        this.completionTime = completionTime;
    }

    @Override
    public String toString() {
        return description + (completed ? " (Completed)" : "");
    }


}
