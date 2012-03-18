package de.smeo.tools.exceptionmonitor.persistence;

import java.util.List;

import org.hibernate.Session;

public class HibernateBasedExceptionDatabase extends ExceptionDatabase {

	@Override
	protected void persist(List<FileExceptionContainer> exceptionDataBase) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        for (FileExceptionContainer currFileExceptionContainer : exceptionDataBase){
        	session.save(currFileExceptionContainer);
        }

        session.getTransaction().commit();
		HibernateUtil.getSessionFactory().close();
	}


}
