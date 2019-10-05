package Model.NetworkModel;

import Model.Appointment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    private ResultStatus resultStatus;
    private String ID;
    private List<Appointment> payload;
    private String message;

    public Result(ResultStatus resultStatus, String ID, List<Appointment> response) {
        this.resultStatus = resultStatus;
        this.ID = ID;
        this.payload = response;
        this.message = setMessage();
    }

    public Result(ResultStatus resultStatus, String ID) {
        this.resultStatus = resultStatus;
        this.ID = ID;
    }

    public Result(ResultStatus resultStatus, List<Appointment> payload) {
        this.resultStatus = resultStatus;
        this.payload = payload;
        this.message = setMessage();
    }

    public Result(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
        this.message = setMessage();
    }

    public Result() {
        this.resultStatus = ResultStatus.FAILURE;
        this.ID = null;
        this.payload = null;
        this.message = null;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<Appointment> getPayload() {
        return payload;
    }

    public void setPayload(List<Appointment> payload) {
        this.payload = payload;
    }

    private String setMessage() {
        switch (resultStatus) {
            case FAILURE:
                return "FAILURE! THIS OPERATION FAILED!";
            case SUCCESS:
                return "SUCCESS! THIS OPERATION SUCCEEDED";
            default:
                return null;
        }
    }

    public String getMessage() {
        return message;
    }

    public byte[] getBytes(Result result) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(result);
        oos.flush();
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    @Override
    public String toString() {
        return "Result {" +
                "resultStatus = " + resultStatus +
                ", ID ='" + ID + '\'' +
                ", message = '" + message + '\'' +
                '}';
    }
}
