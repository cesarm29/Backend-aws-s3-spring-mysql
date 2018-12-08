/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s3api.configuration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author cesar
 */
public class SessionListener implements HttpSessionListener{
    
    private static final int EXPIRATION_TIME_MINUTES = 360;
    private static final int EXPIRATION_TIME_SECONDS = 60;
 
    /**
     * <p>M&eacute;todo que se ejecuta al crear una sesi&oacute;n web.</p>
     * @param event Evento http
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setMaxInactiveInterval(EXPIRATION_TIME_MINUTES*EXPIRATION_TIME_SECONDS);
    }
 
    /**
     * <p>M&eacute;todo que se ejecuta al destruir una sesi&oacute;n web.<>
     * @param event Evento http
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        event.getSession().invalidate();
    }
}
