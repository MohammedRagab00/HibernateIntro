package config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DBConfig {
    //* The issue with this is that it’s outdated.
/*
    public static final SessionFactory SESSION_FACTORY = new Configuration().configure()
            .buildSessionFactory();
*/

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private DBConfig() {
    } // Prevent instantiation

    private static SessionFactory buildSessionFactory() {
        try {

            // 1. Create ServiceRegistry (replaces Configuration)
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // Automatically loads hibernate.cfg.xml
                    .build();

            // 2. Build Metadata (replaces SessionFactory creation)
            Metadata metadata = new MetadataSources(registry)
                    .getMetadataBuilder()
                    .build();

            // 3. Create SessionFactory
            return metadata.getSessionFactoryBuilder().build();

        } catch (Exception ex) {
            System.err.println("SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (SESSION_FACTORY.isClosed()) {
            throw new IllegalStateException("SessionFactory is closed!");
        }
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (SESSION_FACTORY != null && SESSION_FACTORY.isOpen()) {
            SESSION_FACTORY.close();
            System.out.println("Session factory closed");
        }
    }

}
