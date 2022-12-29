package com.aws.file;

import com.aws.model.PartFile;
import com.model.common.MFile;
import com.util.Constant;
import com.util.Folder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class FileUploadUtility {

    private final String upload_path;

    private final FileUploadStrategy fileUploadStrategy;

    /**
     * PR AWS CDN Service
     *
     * @Date 2022-07-27
     * @Author kimwoosik
     * Strategy 패턴 사용
     * LocalFileUploadStrategy or awsFileUploadStrategy 중 선택
     */
    @Autowired
    public FileUploadUtility(@Qualifier("LocalFileUploadStrategy") FileUploadStrategy fileUploadStrategy,
                             ConfigurableWebApplicationContext ctx) {
        upload_path = (String) ctx.getEnvironment().getPropertySources().get("path_props").getProperty("UPLOAD_PATH");
        this.fileUploadStrategy = fileUploadStrategy;
    }

    public MFile uploadFile(MultipartFile file, String cdn_path) {
        try {
            if (file == null || file.getSize() == 0) {
                return null;
            }
            Folder.mkdirs(upload_path);
            String saved_name = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File target = new File(upload_path, saved_name);
            FileCopyUtils.copy(file.getBytes(), target);

            saved_name = fileUploadStrategy.getFileName(saved_name, cdn_path, target);

            MFile mFile = new MFile();
            mFile.setSize(file.getSize());
            mFile.setName(file.getOriginalFilename());
            mFile.setType(file.getContentType());
            mFile.setUrl(saved_name);

            return mFile;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public List<MFile> uploadFiles(List<MultipartFile> files, String cdn_path) {
        List<MFile> mFiles = new ArrayList<>();

        if (files == null) return mFiles;

        for (MultipartFile file : files) {
            MFile mFile = uploadFile(file, cdn_path);
            if (mFile != null) {
                mFiles.add(mFile);
            }
        }
        return mFiles;
    }

    public List<MFile> uploadFiles(MultipartFile[] files, String cdn_path) {
        if (files == null) return new ArrayList<>();
        return uploadFiles(new ArrayList<>(Arrays.asList(files)), cdn_path);
    }
}
