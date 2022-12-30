package com.restcontroller;

import com.aws.file.FileUploadUtility;
import com.aws.model.CDNUploadPath;
import com.model.common.MFile;
import com.response.DefaultRes;
import com.response.Message;
import com.transfer.DownloadBuilder;
import com.util.Folder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/file")
public class FileRestController {
    private final FileUploadUtility fileUploadUtility;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity fileUpload(@RequestBody MultipartFile file) {
        Message message = new Message();
        if (file.getSize() != 0) {
            log.info("file -> {},{},{}", file.getOriginalFilename(), file.getName(), file.getSize());
            MFile mFile = fileUploadUtility.uploadFile(file, CDNUploadPath.BANNER.getPath());
            message.put("file", mFile);
        }
        return new ResponseEntity(DefaultRes.res(HttpStatus.OK, message, true), HttpStatus.OK);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File("C:/Users/zlzld/OneDrive/Desktop/projects/spring-a.2/out/artifacts/webapplication/files/6ca8f750-4e00-4e72-9d46-97ad8e5ac74d_124275812_3694992857224980_6368259687927072062_n.jpg");
        if (file != null) {
            if (new DownloadBuilder().init(response, true).file(file).header().send()) {
                // Download After Logic
            }
        }
    }
}
