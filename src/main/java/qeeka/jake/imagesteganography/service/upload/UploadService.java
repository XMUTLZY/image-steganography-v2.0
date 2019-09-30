package qeeka.jake.imagesteganography.service.upload;

import org.springframework.web.multipart.MultipartFile;
import qeeka.jake.imagesteganography.http.response.BaseResponse;

public interface UploadService {
    BaseResponse uploadImageOss(MultipartFile file);
}
