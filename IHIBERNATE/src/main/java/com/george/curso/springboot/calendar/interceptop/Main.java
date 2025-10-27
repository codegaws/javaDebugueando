package com.george.curso.springboot.calendar.interceptop;

import org.hibernate.Hibernate;
import org.hibernate.query.Query;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        try (var session = HibernateConfig.getSessionFactory().openSession()) {
            final var transaction = session.beginTransaction();

            var weightDivision = DepartmentEntity
                    .builder()
                    .name("Lightweight")
                    .build();

            session.persist(weightDivision);

            var fighter = new EmployeeEntity();
            fighter.setName("Conor McGregor");
            fighter.setEmail("conor@ufc.com");
            fighter.setDepartment(weightDivision);

            session.persist(fighter);

            fighter = new EmployeeEntity();
            fighter.setName("Khabib Nurmagomedov");
            fighter.setEmail("khabib@ufc.com");
            fighter.setDepartment(weightDivision);

            session.persist(fighter);

            transaction.commit();

            Query<EmployeeEntity> query = session.createQuery("FROM EmployeeEntity e JOIN FETCH e.department", EmployeeEntity.class);

            query.list().forEach(System.out::println);
        }
    }
}