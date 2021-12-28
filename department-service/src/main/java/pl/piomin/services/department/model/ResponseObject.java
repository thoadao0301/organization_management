package pl.piomin.services.department.model;

public class ResponseObject {
    private Object data;

    public ResponseObject(Object data) {
        this.data = data;
    }

    public ResponseObject() {
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
