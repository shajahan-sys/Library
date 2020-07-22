package org.library.hibernate_util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HibernateUtilTest {
        private static SessionFactory sessionFactory;
        private static Session session;

        @BeforeAll
        public static void setUp(){
            sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
        }

        @Test
        public void sessionShouldNotBeNull(){
            assertNotNull(session);
        }

        @AfterAll
        public static void tearDown() throws Exception {
            session.close();
            sessionFactory.close();
        }
    }
