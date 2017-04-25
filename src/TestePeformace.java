import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TestePeformace {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        int q = 10000;
        try{
            OrganizadorBrent        brentDatabase           = new OrganizadorBrent("brentDatabase.db");
            OrganizadorSequencial   organizadorSequencial   = new OrganizadorSequencial("sequentialDatabase.db");
            SimpleDateFormat        formatTime              = new SimpleDateFormat("HH:mm:ss.S");
        

            System.out.println("\n\n==> TESTE COM METODO DE BRENT <== ");
            brentDatabase.initDatabase();  // prealoca 
            
            Date startsAt = new Date();
            System.out.println("Inicio dos testes: " + formatTime.format(startsAt));

            for (int i = 0; i < q; i++) {
                Aluno a = new Aluno(i, "Fulano "+i, "Rua 15", "fulano"+i+"@mail.com", (short) 22);
                brentDatabase.addAluno(a);
            }
            Date endsAt = new Date();

            long totalTime = endsAt.getTime() - startsAt.getTime();
            
            System.out.println("\n===============================");
            System.out.println("Iniciado em:     " + formatTime.format(startsAt));
            System.out.println("Finalizado em:   " + formatTime.format(endsAt));
            System.out.println();
            System.out.println("Tempo gasto:     " + totalTime + " ms");

            // ===============================================================
            // ===============================================================

            System.out.println("\n\n==> TESTE COM METODO SEQUENCIAL <== ");

            Date startsAt2 = new Date();
            System.out.println("Inicio dos testes: " + formatTime.format(startsAt2));
            
            for (int i = 0; i < q; i++) {
                Aluno a = new Aluno(i, "Fulano "+i, "Rua 15", "fulano"+i+"@mail.com", (short) 22);
                organizadorSequencial.addAluno(a);
            }

            Date endsAt2 = new Date();

            long totalTime2 = endsAt2.getTime() - startsAt2.getTime();
        
            System.out.println("===============================");
            System.out.println("Iniciado em:     " + formatTime.format(startsAt2));
            System.out.println("Finalizado em:   " + formatTime.format(endsAt2));
            System.out.println();
            System.out.println("Tempo gasto:     " + totalTime2 + " ms");
        
        }
		catch(FileNotFoundException e){
			System.out.println(e);
		}

    }
    
}

        