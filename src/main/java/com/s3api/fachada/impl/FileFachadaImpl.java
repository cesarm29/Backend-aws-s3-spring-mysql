/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s3api.fachada.impl;

import com.s3api.dao.IFileDao;
import com.s3api.fachada.IFileFachada;
import com.s3api.vo.CollectionFileVo;
import com.s3api.vo.FileDownloadVo;
import java.io.InputStream;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cesar
 */
@Service("FileService")
@Transactional
public class FileFachadaImpl implements IFileFachada {

    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(FileFachadaImpl.class);
    @Autowired
    private IFileDao fileDao;

    @Override
    public String uploadFile(String path, String originalFilename, InputStream inp, CollectionFileVo fileVo, Long id) {
        String mensaje = "";
        boolean banderaInsert = true;
        try {
            //guardo archivo
            banderaInsert = banderaInsert && fileDao.uploadFile(path, originalFilename, inp, fileVo, id);
            //valido insert    
            if (banderaInsert) {
                mensaje = "Guardado satisfactoriamente";
            } else {

                mensaje = "Error al guardar archivo";
            }
        } catch (Exception e) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, e.getMessage(), e);
        }
        return mensaje;
    }

    @Override
    public ArrayList<CollectionFileVo> getCollectionFiles() {
        ArrayList<CollectionFileVo> dataFileList = new ArrayList<>();
        dataFileList =  fileDao.getCollectionFiles();
        return  dataFileList;
        
    }

    @Override
    public FileDownloadVo downloadFile(Long idFile) {
        FileDownloadVo dataFileList = new FileDownloadVo();
        dataFileList = fileDao.downloadFile(idFile);

        return dataFileList;
    }

    @Override
    public String deleteFile(Long idFile) {
        String mensaje = "";
        boolean banderaInsert = true;
        try {
            //guardo archivo
            banderaInsert = banderaInsert && fileDao.deleteFile(idFile);
            //valido insert    
            if (banderaInsert) {
                mensaje = "Eliminado satisfactoriamente";
            } else {

                mensaje = "Error al eliminar archivo";
            }
        } catch (Exception e) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, e.getMessage(), e);
        }
        return mensaje;
    }

}
