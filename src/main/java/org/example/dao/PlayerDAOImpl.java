package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.example.exception.ConflictException;
import org.example.exception.DataAccessException;
import org.example.model.Player;
import org.example.util.HibernateUtil;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;

public class PlayerDAOImpl implements PlayerDAO {
    @Override
    public void save(Player player) {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            manager.merge(player);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new ConflictException("Дублирование сущности: " + player);
            }
            throw new DataAccessException("Ошибка сохранения игрока: " + player, e);
        }
    }

    @Override
    public Optional<Player> findById(int id) {
        EntityManager manager = HibernateUtil.getEntityManager();
        try {
            Player player = manager.find(Player.class, id);
            return Optional.ofNullable(player);
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка поиска игрока: " + id, e);
        }
    }

    @Override
    public Optional<Player> findByName(String name) {
        EntityManager manager = HibernateUtil.getEntityManager();
        try {
                     Player p = (Player) manager.createQuery("SELECT p FROM Player p WHERE p.name=:name")
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(p);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка поиска игрока: " + name, e);
        }
    }

    @Override
    public List<Player> findAll() {
        EntityManager manager = HibernateUtil.getEntityManager();
        try {

            return manager.createQuery("SELECT p FROM Player p", Player.class).getResultList();
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка поиска игроков", e);
        }
    }
}
