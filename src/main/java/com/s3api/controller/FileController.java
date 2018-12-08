/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s3api.controller;

import com.amazonaws.services.cloudsearchv2.model.BaseException;
import com.s3api.fachada.IFileFachada;
import com.s3api.vo.CollectionFileVo;
import com.s3api.vo.FileDownloadVo;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author cesar
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(FileController.class);

    @Autowired
    private IFileFachada fileFachada;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json")
    public void uploadFiles(MultipartHttpServletRequest mRequest) {
        Map<String, String[]> map = mRequest.getParameterMap();
        String id = map.get("id")[0];
        Iterator<String> itr = mRequest.getFileNames();
        while (itr.hasNext()) {
            String uuid = UUID.randomUUID().toString();
            String path = "images/" + id + "/files/" + uuid + "/";
            MultipartFile mFile = mRequest.getFile(itr.next());
            CollectionFileVo cfdto = new CollectionFileVo();
            cfdto.setId(Long.valueOf(id));
            cfdto.setByteSize(mFile.getSize());
            cfdto.setFileName(mFile.getOriginalFilename());
            cfdto.setTypeFile(mFile.getContentType());
            cfdto.setFolder(id);
            cfdto.setUuid(uuid);
            cfdto.setInsertDate(new Date());
            cfdto.setPathLocation(path);
            try {
                fileFachada.uploadFile(path, mFile.getOriginalFilename(), mFile.getInputStream(), cfdto, Long.parseLong(id));
            } catch (IOException ex) {
                //throw new BaseException(com.s3api.utils.Error.ERROR_AMAZON_S3_UPLOAD);
            }
        }
    }

    @RequestMapping(value = "/download{id}", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json")
    public void downloadFile(@RequestParam(value = "id") Long id, HttpServletResponse response) throws IOException {
        FileDownloadVo fileDownloadDTO = fileFachada.downloadFile(id);
        response.setContentType(fileDownloadDTO.getCollectionFileVo().getTypeFile());
        response.addHeader("Content-disposition", "attachment");
        response.addHeader("x-filename", fileDownloadDTO.getCollectionFileVo().getFileName());
        response.setContentLength(fileDownloadDTO.getInformation().length);
        OutputStream out = response.getOutputStream();
        out.write(fileDownloadDTO.getInformation());
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/getFiles", method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json")
    public ResponseEntity<ArrayList<CollectionFileVo>> getCollectionFilesFilters() {
        
        ArrayList<CollectionFileVo> dataFile = new ArrayList<>(); 
        dataFile = fileFachada.getCollectionFiles();
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<ArrayList<CollectionFileVo>>(dataFile, headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=*/*", produces = "application/json")
    public void deleteFile(@RequestParam(value = "id") Long id) {
        fileFachada.deleteFile(id);
    }

}
