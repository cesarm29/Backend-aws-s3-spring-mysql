/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s3api.dao;

import com.s3api.vo.CollectionFileVo;
import com.s3api.vo.FileDownloadVo;
import java.io.InputStream;
import java.util.ArrayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author cesar
 */
public interface IFileDao {
    
    public boolean uploadFile(String path, String originalFilename, InputStream inp, CollectionFileVo fileVo, Long id);
    
    public ArrayList<CollectionFileVo> getCollectionFiles();
    
    public FileDownloadVo downloadFile(Long idFile);
    
    public boolean deleteFile (Long idFile);
    
}
