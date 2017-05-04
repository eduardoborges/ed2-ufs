import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;

public class TestePeformace {


    public static void main(String[] args) throws FileNotFoundException, IOException {

        int Q = 1000;

        try {

            // pega os dados a serem buscados e joga numa listinha marota
            File             file   = new File("selected.db");
            RandomAccessFile raf    = new RandomAccessFile(file, "r");
            FileChannel      canal  = raf.getChannel();
            long[] alunos  = new long[1000];

            for (int i = 0; i < Q; i++){
                ByteBuffer buf = ByteBuffer.allocate(8);
                canal.read(buf);
                alunos[i] = buf.getLong(0);
            }

            // vamos preparar algumas coisas...
            // databases necessarias
            OrganizadorBrent        brentDatabase = new OrganizadorBrent("enem_brent_full.db");
            OrganizadorSequencial   seqDatabase   = new OrganizadorSequencial("enem_seq.db");
            
            // formatar os horarios bonitinho
            SimpleDateFormat        formatTime    = new SimpleDateFormat("HH:mm:ss.S");


            /// VAMOS AOS TESTES
            // ===============================================================
            // ===============================================================


            System.out.println("\n==> TESTE COM METODO DE BRENT <== ");
            //brentDatabase.initDatabase();  // prealoca, mas nao precisa
            Date startsAt = new Date();
            System.out.println("Inicio dos testes: " + formatTime.format(startsAt));

            for (long matricula : alunos){
                brentDatabase.getAluno(matricula);
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

            for (long matricula : alunos){
                seqDatabase.getAluno(matricula);
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