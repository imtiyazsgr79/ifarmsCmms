package in.ifarms.com.TaskSearch;

public class TaskSearchResponse {


        private String tno;
        private String lng;
        private String location;
        private String task_number;
        private String lat;

    public TaskSearchResponse() {
    }

    public TaskSearchResponse(String tno, String lng, String location, String task_number, String lat) {
        this.tno = tno;
        this.lng = lng;
        this.location = location;
        this.task_number = task_number;
        this.lat = lat;
    }

    public String getTno ()
        {
            return tno;
        }

        public void setTno (String tno)
        {
            this.tno = tno;
        }

        public String getLng ()
        {
            return lng;
        }

        public void setLng (String lng)
        {
            this.lng = lng;
        }

        public String getLocation ()
        {
            return location;
        }

        public void setLocation (String location)
        {
            this.location = location;
        }

        public String getTask_number ()
        {
            return task_number;
        }

        public void setTask_number (String task_number)
        {
            this.task_number = task_number;
        }

        public String getLat ()
        {
            return lat;
        }

        public void setLat (String lat)
        {
            this.lat = lat;
        }

    }



