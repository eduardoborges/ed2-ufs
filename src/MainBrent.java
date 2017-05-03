
import java.io.*;


public class MainBrent {

    public static void main(String[] args) throws FileNotFoundException {

    
        Aluno a = new Aluno(27, "Maelsu", "Rua 15", "aloha7@mail.com", (short) 22);
        Aluno b = new Aluno(18, "Eduardo", "Rua 10", "aloha5@mail.com", (short) 22);
        Aluno c = new Aluno(29, "Tonho", "Rua 31", "aloha75@mail.com", (short) 22);
        Aluno d = new Aluno(28, "Luandson", "Rua 11", "aloha5@mail.com", (short) 22);
        Aluno e = new Aluno(39, "Laisa", "Rua 49", "laisa@email.com", (short)14);
        Aluno f = new Aluno(13, "Raul", "Rua 11", "laisa@email.com", (short)14);
        Aluno g = new Aluno(16, "Pablo", "Rua 11", "laisa@email.com", (short)14);
        

        String arquivo = "brentDatabase.db";
        OrganizadorBrent database = new OrganizadorBrent(arquivo);
        database.initDatabase();
        database.addAluno(a);
        database.addAluno(b);
        database.addAluno(c);
        database.addAluno(d);
        database.addAluno(e);
        database.addAluno(f);
        database.addAluno(g);
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
