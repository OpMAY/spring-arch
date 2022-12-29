package com.model.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MFile {

    private String url;
    private String name;
    private long size;
    private String type;

    public MFile() {
    }
}
