package qeeka.jake.imagesteganography.service.upload;

import qeeka.jake.imagesteganography.http.response.BaseResponse;

public interface UploadService {
    BaseResponse uploadImage(String imageSuffix);
}
