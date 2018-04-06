package be.ictdynamic.ES_GEO_POC.model;

/**
 * Created by wvdbrand on 12/05/2017.
 */
public class PocResponse {
    private String responseMessage;

    public PocResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
