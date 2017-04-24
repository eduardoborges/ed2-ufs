
import java.io.*;


public class MainBrent {

    public static void main(String[] args) throws FileNotFoundException {

    
        Aluno a = new Aluno(78, "Maelsu", "Rua 15", "aloha7@mail.com", (short) 22);
        Aluno b = new Aluno(16, "Eduardo", "Rua 10", "aloha5@mail.com", (short) 22);
        Aluno c = new Aluno(89, "Tonho", "Rua 31", "aloha75@mail.com", (short) 22);
        Aluno d = new Aluno(1, "Luandson", "Rua 11", "aloha5@mail.com", (short) 22);
        Aluno e = new Aluno(0, "Laisa", "Rua 49", "laisa@email.com", (short)14);
        Aluno f = new Aluno(15, "Raul", "Rua 11", "laisa@email.com", (short)14);
        

        String arquivo = "brentDatabase.db";
        OrganizadorBrent database = new OrganizadorBrent(arquivo);
        // database.initDatabase();
        database.addAluno(a);
        database.addAluno(b);
        database.addAluno(c);
        database.addAluno(d);
        database.addAluno(e);
        database.addAluno(f);
        database.showAllData();
        
        
        // Aluno testeA = database.getAluno(29);
        // if (testeA != null) System.out.println(testeA.getMatric() + " | " + 
        //         testeA.getNome().substring(0,15) + " | " +
        //         testeA.getEmail());
        
        // Aluno del = database.delAluno(27);
        // if (del != null)
        //     System.out.println("O aluno " + del.getNome() + " (" + del.getMatric() + ") foi removido.");
        
        // database.showAllData();

    }
    
}
