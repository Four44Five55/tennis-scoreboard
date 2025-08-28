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
        EntityManager manager = HibernateUtil.getEntityManager();
        try {
            manager.getTransaction().begin(); //старт транзакции. обязательно для всего, что меняет данные
            manager.merge(player);//если запись есть, то он обновит ее, если нет создаст
            manager.getTransaction().commit(); //только здесь вносятся изменения в БД
        } catch (Exception e) {
            //при ошибке откатывем все изменения
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new DuplicateEntityException("Duplicate entity: " + player);
            }
            throw new DataAccessException("Error saving player: " + player, e);
        } finally {
            manager.close();
        }
    }

    @Override
    public Optional<Player> findById(int id) {
        EntityManager manager = HibernateUtil.getEntityManager();
        try {
            Player player = manager.find(Player.class, id);
            return Optional.ofNullable(player);
        } finally {
            manager.close();
        }

    }

    @Override
    public Optional<Player> findByName(String name) {
        EntityManager manager = HibernateUtil.getEntityManager();
        try {
            Player p = (Player) manager.createQuery("SELECT p FROM Player p WHERE p.name=:name")
                    .setParameter("name", name)
                    .getSingleResult(); //один результат
            return Optional.of(p);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Player> findAll() {
        EntityManager manager = HibernateUtil.getEntityManager();
        try {
            return manager.createQuery("SELECT p FROM Player p", Player.class).getResultList();
        } finally {
            manager.close();
        }
    }
}
