package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.example.exception.DataAccessException;
import org.example.entity.Match;
import org.example.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class MatchDAOImpl implements MatchDAO {
    @Override
    public Match save(Match match) {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            manager.merge(match);
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка сохранения матча: " + match, e);
        }
        return match;
    }

    @Override
    public Optional<Match> findById(int id) {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            Match match = manager.find(Match.class, id);
            return Optional.ofNullable(match);
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка поиска матча по id: " + id, e);
        }
    }

    @Override
    public List<Match> findAll() {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            return manager.createQuery("SELECT m FROM Match m", Match.class).getResultList();
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка поиска всех матчей", e);
        }
    }

    @Override
    public List<Match> findByPlayerName(String name) {
        String jpql = "SELECT m FROM Match m " +
                "JOIN m.player1 p1 JOIN m.player2 p2 " +
                "WHERE LOWER(p1.name) LIKE :pattern OR LOWER(p2.name) LIKE :pattern";

        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            String pattern = "%" + name.toLowerCase() + "%";
            return manager.createQuery(jpql, Match.class)
                    .setParameter("pattern", pattern)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка поиска матчей по имени игрока: " + name, e);
        }
    }

    @Override
    public List<Match> findPaginated(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String jpql = "SELECT m FROM Match m ORDER BY m.id DESC";

        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            return manager.createQuery(jpql, Match.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка при поиске матчей с пагинацией (страница " + page + ")", e);
        }
    }

    @Override
    public long countAll() {
        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            Long count = manager.createQuery("SELECT COUNT(m) FROM Match m", Long.class)
                    .getSingleResult();
            return count != null ? count : 0L;
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка подсчёта матчей", e);
        }
    }

    @Override
    public List<Match> findPaginatedByPlayerName(String name, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String jpql = "SELECT m FROM Match m " +
                "JOIN m.player1 p1 JOIN m.player2 p2 " +
                "WHERE LOWER(p1.name) LIKE :pattern OR LOWER(p2.name) LIKE :pattern " +
                "ORDER BY m.id DESC";

        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            String pattern = "%" + name.toLowerCase() + "%";
            return manager.createQuery(jpql, Match.class)
                    .setParameter("pattern", pattern)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка поиска матчей по имени игрока с пагинацией: " + name, e);
        }
    }

    @Override
    public long countByPlayerName(String name) {
        String jpql = "SELECT COUNT(m) FROM Match m " +
                "JOIN m.player1 p1 JOIN m.player2 p2 " +
                "WHERE LOWER(p1.name) LIKE :pattern OR LOWER(p2.name) LIKE :pattern";

        try {
            EntityManager manager = HibernateUtil.getEntityManager();
            String pattern = "%" + name.toLowerCase() + "%";
            return manager.createQuery(jpql, Long.class)
                    .setParameter("pattern", pattern)
                    .getSingleResult();
        } catch (PersistenceException e) {
            throw new DataAccessException("Ошибка подсчета матчей по имени игрока: " + name, e);
        }
    }
}

