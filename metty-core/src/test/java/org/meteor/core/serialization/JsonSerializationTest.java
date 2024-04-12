package org.meteor.core.serialization;

import lombok.Data;
import org.meteor.metty.core.serialization.json.JsonSerialization;

import java.util.Arrays;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: JsonSerializatonTest
 * @Created Time: 2024-04-02 20:14
 **/

@Data
class Student{
    int age ;
    String name;

    public Student(int age, String name) {
        this.age = age;
        this.name = name;
    }

}


public class JsonSerializationTest {
    public static void main(String[] args) {
        JsonSerialization js = new JsonSerialization();
        Student stud = new Student(12,"fuck");
        byte[] serialize = js.serialize(stud);
        System.out.println(Arrays.toString(serialize));
        Student new_stud = (Student) js.deserialize(Student.class,serialize);
        System.out.println(new_stud);
    }
}
