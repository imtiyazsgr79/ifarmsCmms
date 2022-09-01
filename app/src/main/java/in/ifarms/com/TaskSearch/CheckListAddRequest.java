package in.ifarms.com.TaskSearch;

public class CheckListAddRequest {
    private String checklistvalue;

    private String checklistkey;

    private String taskId;

    public String getChecklistvalue ()
    {
        return checklistvalue;
    }

    public void setChecklistvalue (String checklistvalue)
    {
        this.checklistvalue = checklistvalue;
    }

    public String getChecklistkey ()
    {
        return checklistkey;
    }

    public void setChecklistkey (String checklistkey)
    {
        this.checklistkey = checklistkey;
    }

    public String getTaskId ()
    {
        return taskId;
    }

    public void setTaskId (String taskId)
    {
        this.taskId = taskId;
    }

    public CheckListAddRequest(String checklistvalue, String checklistkey, String taskId) {
        this.checklistvalue = checklistvalue;
        this.checklistkey = checklistkey;
        this.taskId = taskId;
    }
}

