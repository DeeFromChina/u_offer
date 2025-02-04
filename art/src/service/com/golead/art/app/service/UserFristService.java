package com.golead.art.app.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.golead.core.exception.ServiceException;

/**
 *
 * @author 莫景华
 * @version 1.0
 * @since 2016年11月1日上午9:30:36
 * 
 */
public interface UserFristService {

   public List<Map<String, Object>> appUser(Map<String, String> parameters) throws ServiceException;
   
   public String addUser(Map<String, String> parameters, File photo) throws ServiceException;
}
