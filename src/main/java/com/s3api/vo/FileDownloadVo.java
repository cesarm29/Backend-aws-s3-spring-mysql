/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s3api.vo;

/**
 *
 * @author cesar
 */
public class FileDownloadVo {
    
    private byte [] information;
    
    private CollectionFileVo collectionFileVo;

    public FileDownloadVo() {
    }

    public byte[] getInformation() {
        return information;
    }

    public void setInformation(byte[] information) {
        this.information = information;
    }

    public CollectionFileVo getCollectionFileVo() {
        return collectionFileVo;
    }

    public void setCollectionFileVo(CollectionFileVo collectionFileVo) {
        this.collectionFileVo = collectionFileVo;
    }
    
}
