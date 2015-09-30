package com.github.theborakompanioni.gn.shiro.geode;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.Region;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;

/**
 * Data Access Object for Shiro {@link Session} persistence in Hazelcast.
 */
public class GeodeSessionDao extends AbstractSessionDAO {

    private static final Logger log = LoggerFactory
            .getLogger(GeodeSessionDao.class);
    private Cache cache;

    public GeodeSessionDao(final Cache cache) {
        this.cache = cache;
    }

    @Override
    protected Serializable doCreate(Session session) {
        final Serializable sessionId = generateSessionId(session);
        log.debug("Creating a new session identified by[{}]", sessionId);
        assignSessionId(session, sessionId);
        getSessions().put(session.getId(), session);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        log.debug("Reading a session identified by[{}]", sessionId);
        return getSessions().get(sessionId);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        log.debug("Updating a session identified by[{}]", session.getId());
        getSessions().replace(session.getId(), session);
    }

    @Override
    public void delete(Session session) {
        log.debug("Deleting a session identified by[{}]", session.getId());
        getSessions().remove(session.getId());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return getSessions().values();
    }


    private Region<Serializable, Session> getSessions() {
        return cache.getRegion("sessions");
    }

}
