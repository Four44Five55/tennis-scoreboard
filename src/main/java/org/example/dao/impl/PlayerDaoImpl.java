package org.example.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.example.dao.PlayerDAO;
import org.example.exception.DataAccessException;
import org.example.exception.DuplicateEntityException;
import org.example.model.Player;
import org.example.util.HibernateUtil;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;

public class PlayerDaoImpl implements PlayerDAO {
    @Override
    public void save(Player player) {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            manager.merge(player);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new DuplicateEntityException("Duplicate entity: " + player);
            }
            throw new DataAccessException("Error saving player: " + player, e);
        }
    }

    @Override
    public Optional<Player> findById(int id) {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            Player player = manager.find(Player.class, id);
            return Optional.ofNullable(player);
        } catch (PersistenceException e) {
            throw new DataAccessException("Error finding player: " + id, e);
        }
    }

    @Override
    public Optional<Player> findByName(String name) {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            Player p = (Player) manager.createQuery("SELECT p FROM Player p WHERE p.name=:name")
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(p);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (PersistenceException e) {
            throw new DataAccessException("Error finding player: " + name, e);
        }
    }

    @Override
    public List<Player> findAll() {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            return manager.createQuery("SELECT p FROM Player p", Player.class).getResultList();
        } catch (PersistenceException e) {
            throw new DataAccessException("Error finding players", e);
        }
    }
}
