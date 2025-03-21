package config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Config {

  public static final SessionFactory SESSION_FACTORY = new Configuration().configure()
      .buildSessionFactory();

  public void shutdown() {
    if (SESSION_FACTORY.isOpen()) {
      SESSION_FACTORY.close();
    }
  }
}
