/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s3api.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author cesar
 */
public abstract class AbstractDao {
    
    
    @Autowired
    private SessionFactory sessionFactory;
    
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public void persist(Object entity) {
        getSession().persist(entity);
    }
    
    public void delete(Object entity) {
        getSession().delete(entity);
    }
    
    public void flush() {
        getSession().flush();
    }
     
    public void update(Object entity) {
        getSession().saveOrUpdate(entity);
    }
}
