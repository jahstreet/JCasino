package by.sasnouskikh.jcasino.service;

import by.sasnouskikh.jcasino.dao.impl.DAOHelper;
import by.sasnouskikh.jcasino.db.ConnectionPool;

abstract class AbstractService implements AutoCloseable {

    /**
     * DAOHelper instance for this class instance use.
     */
    DAOHelper daoHelper;

    /**
     * Default instance constructor.
     */
    AbstractService() {
        daoHelper = new DAOHelper();
    }

    /**
     * Constructs instance using definite {@link DAOHelper} object.
     */
    AbstractService(DAOHelper daoHelper) {
        this.daoHelper = daoHelper;
    }

    /**
     * Returns {@link DAOHelper#connection} to {@link ConnectionPool}.
     */
    @Override
    public void close() {
        daoHelper.close();
    }
}