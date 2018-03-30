package be.ictdynamic.ES_GEO_POC.model;

/**
 * Created by wvdbrand on 12/05/2017.
 */
public class LocationResponse {
    private String responseMessage;

    public LocationResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
