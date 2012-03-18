package de.smeo.tools.exceptionmonitor.persistence;

import java.util.List;

import org.hibernate.Session;

import de.smeo.tools.exceptionmonitor.domain.FileExceptionContainer;

public class HibernateBasedExceptionDatabase extends ExceptionDatabase {

	@Override
	protected void persist(List<FileExceptionContainer> exceptionDataBase) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        for (FileExceptionContainer currFileExceptionContainer : exceptionDataBase){
        	session.saveOrUpdate(currFileExceptionContainer);
        }

        session.getTransaction().commit();
		HibernateUtil.getSessionFactory().close();
	}


}
