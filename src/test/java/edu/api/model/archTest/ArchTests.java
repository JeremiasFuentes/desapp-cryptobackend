package edu.api.model.archTest;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures.LayeredArchitecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;

public class ArchTests {

    private JavaClasses jc;

    @BeforeEach
    public void setup() {
        jc = new ClassFileImporter()
                .importPackages("edu.api.model");
    }

    @Test
    public void Services_should_only_be_accessed_by_Controllers() {

        ArchRule myRule = classes()
                .that().resideInAPackage("..service..")
                .should().onlyBeAccessed().byAnyPackage("..controller..", "..service..", "..util..", "..unitTest..", "..jwt..");

        myRule.check(jc);
    }

    @Test
    void repositoryClassesShouldBeNamedXRepository() {

        classes()
                .that().resideInAPackage("..repository..")
                .should().haveSimpleNameEndingWith("Repository")
                .check(jc);
    }

    @Test
    void jwtClassesShouldBeNamedjwtX() {

        classes()
                .that().resideInAPackage("..jwt..")
                .should().haveSimpleNameStartingWith("Jwt")
                .check(jc);
    }

    @Test
    void serviceClassesShouldBeNamedXService() {

        classes()
                .that().resideInAPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .check(jc);
    }

    @Test
    void controllerClassesShouldBeNamedXController() {

        classes()
                .that().resideInAPackage("..controller..")
                .should().haveSimpleNameEndingWith("Controller")
                .check(jc);
    }

    @Test
    void repositoryClassesShouldHaveSpringRepositoryAnnotation() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().beAnnotatedWith(Repository.class)
                .check(jc);
    }

    @Test
    void serviceClassesShouldHaveSpringServiceAnnotation() {
        classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith(Service.class)
                .check(jc);
    }

    @Test
    void entityClassesShouldHaveSpringEntityAnnotation() {
        classes()
                .that().resideInAPackage("..entity..")
                .should().beAnnotatedWith(Entity.class)
                .check(jc);
    }

    @Test
    void controllerClassesShouldHaveSpringControllerAnnotation() {
        classes()
                .that().resideInAPackage("..controller..")
                .should().beAnnotatedWith(RestController.class)
                .check(jc);
    }

    @Test
    void configClassesShouldBeNamedXConfig() {

        classes()
                .that().resideInAPackage("..config..")
                .should().haveSimpleNameEndingWith("Config")
                .check(jc);
    }

    @Test
    void configClassesShouldHaveSpringConfigAnnotation() {
        classes()
                .that().resideInAPackage("..config..")
                .should().beAnnotatedWith(Configuration.class)
                .check(jc);
    }

    @Test
    public void noClassesWithServiceAnnotationShouldResideOutsideTheServiceLayer() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().areAnnotatedWith(Service.class)
                .should().resideOutsideOfPackage("..service..");
        rule.check(jc);
    }

    @Test
    public void noClassesWithRepositoryAnnotationShouldResideOutsideTheRepositoryLayer() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().areAnnotatedWith(Repository.class)
                .should().resideOutsideOfPackage("..repository..");
        rule.check(jc);
    }

    @Test
    public void noClassesWithControllerAnnotationShouldResideOutsideTheControllerLayer() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().areAnnotatedWith(RestController.class)
                .should().resideOutsideOfPackage("..controller..");
        rule.check(jc);
    }
}
