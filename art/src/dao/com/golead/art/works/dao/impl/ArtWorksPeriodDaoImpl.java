package com.golead.art.works.dao.impl;

import java.io.Serializable;
import com.golead.core.exception.DAOException;
import com.golead.core.dao.impl.HibernateDaoImpl;
import com.golead.art.works.model.ArtWorksPeriod;
import com.golead.art.works.dao.ArtWorksPeriodDao;
import org.springframework.stereotype.Repository;

@Repository
public class ArtWorksPeriodDaoImpl extends HibernateDaoImpl<ArtWorksPeriod> implements ArtWorksPeriodDao {

   public Class getReferenceClass() {
      return ArtWorksPeriod.class;
   }

   public ArtWorksPeriod cast(Object object) {
      return (ArtWorksPeriod) object;
   }

   public ArtWorksPeriod get(Serializable id)  throws DAOException{
      return (ArtWorksPeriod) super.get(id);
   }

   public ArtWorksPeriod load(Serializable id)  throws DAOException{
      return (ArtWorksPeriod) super.load(id);
   }

   public Serializable save(ArtWorksPeriod artWorksPeriod)  throws DAOException{
      return super.save(artWorksPeriod);
   }

   public void saveOrUpdate(ArtWorksPeriod artWorksPeriod)  throws DAOException{
      super.saveOrUpdate(artWorksPeriod);
   }

   public void update(ArtWorksPeriod artWorksPeriod)  throws DAOException{
      super.update(artWorksPeriod);
   }

   public void delete(ArtWorksPeriod artWorksPeriod)  throws DAOException{
      super.delete(artWorksPeriod);
   }

   public void refresh(ArtWorksPeriod artWorksPeriod)  throws DAOException{
      super.refresh(artWorksPeriod);
   }

   public String getTableName() {
      return ArtWorksPeriod.REF_TABLE;
   }
}
