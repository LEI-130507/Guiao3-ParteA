package iscte.ista;

public class Student {
    private String name;
    private int number;

    public Student() {
    }

    public Student(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Retorna a representação do estudante no formato CSV: número,nome
     */
    public String toCSV() {
        return number + "," + name;
    }

    @Override
    public String toString() {
        return "Student{number=" + number + ", name='" + name + "'}";
    }
}