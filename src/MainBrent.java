/* *****************************************************************************
 * Códigos desenvolvidos pelos seguintes alunos
 *
 * @author Claudson Bispo Martins Santos    201410042132
 * @author Edgar Vieira Lima Neto           201410042150
 * @author Allan silva Santos               201410060220
 * ****************************************************************************/

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainBrent {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException,
                                                  IOException {
        // Obtém o momento em que o algoritmo começou a ser processado
        // long startTime = System.currentTimeMillis();
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
        Date startTime = new Date();
        System.out.println("Início em: " + ft.format(startTime));
        
        // Instancia manualmente 3 alunos
        Aluno a = new Aluno(78, "Maelsu", "Rua 15", "aloha7@mail.com", (short) 22);
        Aluno b = new Aluno(16, "Eduardo", "Rua 10", "aloha5@mail.com", (short) 22);
        Aluno c = new Aluno(89, "Tonho", "Rua 31", "aloha75@mail.com", (short) 22);
        Aluno d = new Aluno(1, "Luandson", "Rua 11", "aloha5@mail.com", (short) 22);
        Aluno e = new Aluno(0, "Laisa", "Rua 49", "laisa@email.com", (short)14);
        Aluno f = new Aluno(15, "Raul", "Rua 11", "laisa@email.com", (short)14);
        Aluno vazio = new Aluno(-1, "[VAZIO]", "[VAZIO]", "[VAZIO]", (short) 0);

        String arquivo = "brentDatabase.db";
        OrganizadorBrent database = new OrganizadorBrent(arquivo);
        //teste.inicializaArquivo(vazio);
        

        // Persiste os alunos instanciados manualmente no arquivo
        database.addAluno(a);
        database.addAluno(b);
        database.addAluno(c);
        database.addAluno(d);
        database.addAluno(e);
        database.addAluno(f);
        database.listarArquivo();
        
        Aluno testeA = database.getAluno(29);
        if (testeA != null) System.out.println(testeA.getMatric() + " | " + 
                testeA.getNome().substring(0,15) + " | " +
                testeA.getEmail());
        
        Aluno del = database.delAluno(27);
        if (del != null)
            System.out.println("O aluno " + del.getNome() + " (" + del.getMatric() + ") foi removido.");
        
        database.listarArquivo();
        
        
        // Instancia e persiste automaticamente 1 Milhão de registros no arquivo
        /*Aluno novo = null;
        for (int i = 1; i <= 1000000; i++) {
            novo = new Aluno(i, String.valueOf(i), "Rua 6, 45", (short) 14, "M", "email@com");
            teste.addAluno(novo);
        }*/
        
        // Lê as matriculas a serem buscadas
        /*ManipuladorSequencial teste2 = new ManipuladorSequencial("data\\selected.db");
        int[] selected = teste2.lerSelecionados();*/
        
        // Realiza a busca sequencial das matriculas
        /*for (int i = 0; i < 1000; i++) {
            Aluno a = teste.getAluno(selected[i]);
            if (a != null) System.out.println("[ " + i + " ] " +
                    a.getMatricula() + " | " + 
                    a.getNome().substring(0,15) + " | " +
                    a.getEmail());
        }*/
        
        // Migra os alunos do ENEM_ALEAT para o ENEM_BRENT
        // ManipuladorSequencial teste2 = new ManipuladorSequencial("data\\enem_aleat.db");
        // for (int i = 0; i < 8722356; i++) {
        //     Aluno a = new Aluno(teste2.lerAluno(i * 157));
        //     System.out.println("[ " + i + " ] " +
        //             a.getMatricula() + " | " + 
        //             a.getNome().substring(0,15) + " | " +
        //             a.getEmail());
        //     teste.addAluno(a);
        // }
  
        // Obtém do arquivo os alunos que foram instanciados manualmente
        /*Aluno b = teste.getAluno(8);
        if (b != null) System.out.println(b.getMatricula() + " | " + 
                                          b.getNome().substring(0,15) + " | " +
                                          b.getEmail());
        b = teste.getAluno(15);
        if (b != null) System.out.println(b.getMatricula() + " | " + 
                                          b.getNome().substring(0,15) + " | " +
                                          b.getEmail());
        b = teste.getAluno(7);
        if (b != null) System.out.println(b.getMatricula() + " | " + 
                                          b.getNome().substring(0,15) + " | " +
                                          b.getEmail());*/

        // Deleta do arquivo os alunos que foram instanciados manualmente
        /*Aluno del = teste.delAluno(8);
        if (del != null)
            System.out.println("O aluno " + del.getNome() +
                               " (" + del.getMatricula() + ") foi removido.");
        del = teste.delAluno(15);
        if (del != null)
            System.out.println("O aluno " + del.getNome() +
                               " (" + del.getMatricula() + ") foi removido.");
        del = teste.delAluno(7);
        if (del != null)
            System.out.println("O aluno " + del.getNome() +
                               " (" + del.getMatricula() + ") foi removido.");*/

        // Obtém o momento em que o algoritmo terminou de ser processado
        // long endTime   = System.currentTimeMillis();
        Date endTime = new Date();
        // Calcula e imprime o tempo total de execução em milisegundos
        long totalTime = endTime.getTime() - startTime.getTime();
        
        // Computa a diferença detalhadamente
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliSecondsRest = totalTime;
        for (TimeUnit unit : units) {
            long diff = unit.convert(milliSecondsRest,TimeUnit.MILLISECONDS);
            long diffInMilliSecondsForUnit = unit.toMillis(diff);
            milliSecondsRest = milliSecondsRest - diffInMilliSecondsForUnit;
            result.put(unit,diff);
        }
        
        System.out.println("---------------------");
        System.out.println("Hora de Início:    " + ft.format(startTime));
        System.out.println("Hora do Término:   " + ft.format(endTime));
        System.out.println("");
        System.out.println("Tempo de Execução: " + totalTime + " ms");
        /*System.out.println("                   " + (totalTime/1000) + " s");
        System.out.println("                   " +
                result.get(TimeUnit.HOURS) + " h : " +
                result.get(TimeUnit.MINUTES) + " min : " +
                result.get(TimeUnit.SECONDS) + " s");*/
    }
    
}
