package iscte.ista;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class App {

    private static final String FIRSTNAMES_F_FILE = "data/firstnames_f.json";
    private static final String FIRSTNAMES_M_FILE = "data/firstnames_m.json";
    private static final String SURNAMES_FILE = "data/surnames.json";
    private static final String STUDENTS_CVS_FILE = "data/students.cvs";
    private static final int INITIAL_NUMBER = 21000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Indique o número de estudantes a gerar: ");
        int totalStudents;

        try {
            totalStudents = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Valor inválido. A terminar.");
            return;
        }

        if (totalStudents <= 0) {
            System.out.println("O número de estudantes deve ser superior a zero.");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            // 1. Ler listas dos ficheiros JSON usando Jackson
            List<String> firstnamesF = mapper.readValue(new File(FIRSTNAMES_F_FILE), new TypeReference<List<String>>() {});
            List<String> firstnamesM = mapper.readValue(new File(FIRSTNAMES_M_FILE), new TypeReference<List<String>>() {});
            List<String> surnames = mapper.readValue(new File(SURNAMES_FILE), new TypeReference<List<String>>() {});

            // Juntar todos os primeiros nomes numa única lista
            List<String> allFirstNames = new ArrayList<>(firstnamesF);
            allFirstNames.addAll(firstnamesM);

            long maxPossibleCombinations = (long) allFirstNames.size() * surnames.size();
            if (totalStudents > maxPossibleCombinations) {
                System.err.println("Erro: Não é possível gerar " + totalStudents
                        + " estudantes únicos. Limite máximo: " + maxPossibleCombinations);
                return;
            }

            // 2. Gerar nomes únicos aleatoriamente
            Random random = new Random();
            Set<String> usedNames = new HashSet<>();
            List<Student> students = new ArrayList<>(totalStudents);

            int currentNumber = INITIAL_NUMBER;
            while (students.size() < totalStudents) {
                String firstName = allFirstNames.get(random.nextInt(allFirstNames.size()));
                String surname = surnames.get(random.nextInt(surnames.size()));
                String fullName = firstName + " " + surname;

                if (usedNames.add(fullName)) { // add retorna true apenas se for único
                    students.add(new Student(fullName, currentNumber++));
                }
            }

            // 3. Garantir que a diretoria 'data/' existe
            File outputFile = new File(STUDENTS_CVS_FILE);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // 4. Gravar ficheiro CSV com cabeçalho
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                writer.println("numero,nome");
                for (Student s : students) {
                    writer.println(s.toCSV());
                }
            }

            System.out.println("Foram gerados com sucesso " + totalStudents
                    + " estudantes no ficheiro: " + STUDENTS_CVS_FILE);

        } catch (Exception e) {
            System.err.println("Erro ao processar ficheiros: " + e.getMessage());
            e.printStackTrace();
        }
    }
}