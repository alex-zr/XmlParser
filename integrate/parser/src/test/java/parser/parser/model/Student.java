package parser.parser.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 14.03.12
 */
public class Student {
    private String name;
    private Group group;
    private List addresses;
    int age;

    public Student() {
    }

    public Student(String name, Group group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List getAddresses() {
        return addresses;
    }

    public void setAddresses(List addresses) {
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (age != student.age) return false;
        if (addresses != null ? !addresses.equals(student.addresses) : student.addresses != null) return false;
        if (group != null ? !group.equals(student.group) : student.group != null) return false;
        if (name != null ? !name.equals(student.name) : student.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (addresses != null ? addresses.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", group=" + group +
                ", addresses=" + addresses +
                ", age=" + age +
                '}';
    }
}
