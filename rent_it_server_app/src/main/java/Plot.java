import java.util.List;

public class Plot {
    private String plot_no;
    private String plot_id;
    private List<String> occupied_time;
    private boolean is_occupied;
    private String parent;
    private boolean is_open;
    private List<String> occupied_user;
    private int priority;

    public Plot() {
        //empty constructor needed
    }


    public Plot(String plot_no,
                String plot_id,
                List<String> occupied_time,
                boolean is_occupied,
                String parent,
                boolean is_open, List<String> occupied_user, int priority) {
        this.plot_no = plot_no;
        this.plot_id = plot_id;
        this.occupied_time = occupied_time;
        this.is_occupied = is_occupied;
        this.parent = parent;
        this.is_open = is_open;
        this.occupied_user = occupied_user;
        this.priority = priority;
    }

    public String getPlot_no() {
        return plot_no;
    }

    public void setPlot_no(String plot_no) {
        this.plot_no = plot_no;
    }

    public String getPlot_id() {
        return plot_id;
    }

    public void setPlot_id(String plot_id) {
        this.plot_id = plot_id;
    }

    public List<String> getOccupied_time() {
        return occupied_time;
    }

    public void setOccupied_time(List<String> occupied_time) {
        this.occupied_time = occupied_time;
    }

    public boolean isIs_occupied() {
        return is_occupied;
    }

    public void setIs_occupied(boolean is_occupied) {
        this.is_occupied = is_occupied;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    public List<String> getOccupied_user() {
        return occupied_user;
    }

    public void setOccupied_user(List<String> occupied_user) {
        this.occupied_user = occupied_user;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
