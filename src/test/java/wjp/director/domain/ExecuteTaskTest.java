package wjp.director.domain;

import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ExecuteTaskTest {

    @org.junit.jupiter.api.Test
    void doHandler() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Student student = new Student();
        Method getName = Student.class.getMethod("getName");
        Object[] para = new Object[0];
        String invoke = (String)getName.invoke(student, para);
        Assert.assertEquals(invoke, "wjp");
    }

    class Student extends People{
        String defaultName = "wjp";
        public String getName() {
            return defaultName;
        }
    }
    class People {
        public People() {
            System.out.println(this.getClass().getSimpleName());
        }
    }
    @org.junit.jupiter.api.Test
    public void testFather() {
        new Student();
    }
}