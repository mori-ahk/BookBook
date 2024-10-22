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

    public Result(ResultStatus resultStatus, String ID, List<Appointment> response) {
        this.resultStatus = resultStatus;
        this.ID = ID;
        this.payload = response;
    }

    public Result(ResultStatus resultStatus, String ID) {
        this.resultStatus = resultStatus;
        this.ID = ID;
    }

    public Result(ResultStatus resultStatus, List<Appointment> payload) {
        this.resultStatus = resultStatus;
        this.payload = payload;
    }

    public Result(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public Result() {
        this.resultStatus = ResultStatus.FAILURE;
        this.ID = null;
        this.payload = null;
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
                ", ID = '" + ID + '\'' +
                ", payload = " + payload +
                '}';
    }
}
