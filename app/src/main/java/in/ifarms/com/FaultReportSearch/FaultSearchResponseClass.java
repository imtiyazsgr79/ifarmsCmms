package in.ifarms.com.FaultReportSearch;

public class FaultSearchResponseClass {


        private String count;

        private String[] list;

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        public String[] getList ()
        {
            return list;
        }

        public void setList (String[] list)
        {
            this.list = list;
        }





    public class List
    {
        private String createdDate;

        private String reportedDate;

        private String frId;

        private String location;

        private String building;

        private String status;

        public String getCreatedDate ()
        {
            return createdDate;
        }

        public void setCreatedDate (String createdDate)
        {
            this.createdDate = createdDate;
        }

        public String getReportedDate ()
        {
            return reportedDate;
        }

        public void setReportedDate (String reportedDate)
        {
            this.reportedDate = reportedDate;
        }

        public String getFrId ()
        {
            return frId;
        }

        public void setFrId (String frId)
        {
            this.frId = frId;
        }

        public String getLocation ()
        {
            return location;
        }

        public void setLocation (String location)
        {
            this.location = location;
        }

        public String getBuilding ()
        {
            return building;
        }

        public void setBuilding (String building)
        {
            this.building = building;
        }

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

    }



}


