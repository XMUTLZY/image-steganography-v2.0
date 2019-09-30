package qeeka.jake.imagesteganography.http.response;

import java.util.Map;

public class ImageResultResponse extends BaseResponse {
    private Map<String, String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
