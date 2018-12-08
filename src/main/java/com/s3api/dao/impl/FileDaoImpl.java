/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s3api.dao.impl;

import com.s3api.dao.AbstractDao;
import com.s3api.dao.IFileDao;
import com.s3api.vo.CollectionFileVo;
import com.s3api.vo.FileDownloadVo;
import com.google.common.io.ByteStreams;
import java.io.InputStream;
import java.util.ArrayList;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cesar
 */
@Repository("FileDao")
@PropertySources({
    @PropertySource("classpath:common.properties")
    ,
@PropertySource("classpath:application-${env}.properties")})
public class FileDaoImpl extends AbstractDao implements IFileDao {

    @Autowired
    private AmazonS3 s3client;
    @Value("${bucket}")
    private String bucketName;

    @Value("${environment}")
    private String environment;

    @Autowired
    private TransferManager transferManager;

    @Override
    public boolean uploadFile(String path, String originalFilename, InputStream inp, CollectionFileVo fileVo, Long id) {
        try {
            String pathWithCompany = environment + "/" + path + originalFilename;
            PutObjectRequest request = new PutObjectRequest(bucketName, pathWithCompany, inp, new ObjectMetadata());
            request.setGeneralProgressListener((ProgressEvent progressEvent) -> {
                String transferredBytes = "Uploaded bytes: " + progressEvent.getBytesTransferred();
            });
            Upload upload = transferManager.upload(request);
            upload.waitForCompletion();

            if (upload.isDone()) {
                //mesage
            }

        } catch (AmazonServiceException ex) {
            Logger.getLogger(FileDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AmazonClientException ex) {
            Logger.getLogger(FileDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(FileDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public ArrayList<CollectionFileVo> getCollectionFiles() {
        //array list FileDownloadVo
        ArrayList<CollectionFileVo> dataList = new ArrayList<>();
        //init session 
        Session session = getSession();
        //select the file
        String squeryFile = "SELECT id, file_name, type_file, folder, byte_size, path, uuid, insert_date   FROM `s3`   ";
        SQLQuery queryFile = session.createSQLQuery(squeryFile);
        ArrayList<Object[]> dataObjectFile = (ArrayList<Object[]>) queryFile.list();
        //cicle for objects SearchDataVo
        for (Object[] object : dataObjectFile) {
            CollectionFileVo dataType = mapeoGetDataFile(object);
            dataList.add(dataType);
        }

        return dataList;
    }

    @Override
    public FileDownloadVo downloadFile(Long idFile) {
        //dto
        FileDownloadVo fileDownloadVo = new FileDownloadVo();
        //array list FileDownloadVo
        ArrayList<CollectionFileVo> dataList = new ArrayList<>();
        try {
            //init session 
            Session session = getSession();
            //select the file
            String squeryFile = "SELECT id, file_name, type_file, folder, byte_size, path, uuid, insert_date   FROM `s3` where id = " + idFile + "  ";
            SQLQuery queryFile = session.createSQLQuery(squeryFile);
            ArrayList<Object[]> dataObjectFile = (ArrayList<Object[]>) queryFile.list();
            //cicle for objects SearchDataVo
            for (Object[] object : dataObjectFile) {
                CollectionFileVo dataType = mapeoGetDataFile(object);
                dataList.add(dataType);
            }

            String pathWithCompany = environment + "/" + dataList.get(0).getPathLocation() + dataList.get(0).getFileName();
            S3Object object = s3client.getObject(new GetObjectRequest(bucketName, pathWithCompany));
            S3ObjectInputStream in = object.getObjectContent();
            byte[] bytes = ByteStreams.toByteArray(in);
            fileDownloadVo.setInformation(bytes);

        } catch (Exception ex) {
            Logger.getLogger(FileDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fileDownloadVo;
    }

    @Override
    public boolean deleteFile(Long idFile) {
        //array list FileDownloadVo
        ArrayList<CollectionFileVo> dataList = new ArrayList<>();
        //init session 
        Session session = getSession();
        //select the file
        String squeryFile = "SELECT id, file_name, type_file, folder, byte_size, path, uuid, insert_date   FROM `s3` where id = " + idFile + "  ";
        SQLQuery queryFile = session.createSQLQuery(squeryFile);
        ArrayList<Object[]> dataObjectFile = (ArrayList<Object[]>) queryFile.list();
        //cicle for objects SearchDataVo
        for (Object[] object : dataObjectFile) {
            CollectionFileVo dataType = mapeoGetDataFile(object);
            dataList.add(dataType);
        }

        String pathWithCompany = environment + "/" + dataList.get(0).getPathLocation() + dataList.get(0).getFileName();
        s3client.deleteObject(new DeleteObjectRequest(bucketName, pathWithCompany));

        return false;
    }

    //generate mapping from data file
    public CollectionFileVo mapeoGetDataFile(Object[] retorno) {
        CollectionFileVo data = new CollectionFileVo();
        data.setId(retorno[0] != null ? (Long) retorno[0] : 0);
        data.setFileName(retorno[1] != null ? retorno[1].toString() : null);
        data.setTypeFile(retorno[2] != null ? retorno[2].toString() : null);
        data.setFolder(retorno[3] != null ? retorno[3].toString() : null);
        data.setByteSize(retorno[4] != null ? (Long) retorno[4] : 0);
        data.setPathLocation(retorno[5] != null ? retorno[5].toString() : null);
        data.setUuid(retorno[6] != null ? retorno[6].toString() : null);
        data.setInsertDate(retorno[7] != null ? (Date) retorno[7] : null);
        return data;
    }

}
