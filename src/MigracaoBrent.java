
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
public class MigracaoBrent{
    public static void main(String[] args) throws FileNotFoundException, IOException{
         File fOrigem = new File("enem_aleat.db");
         RandomAccessFile fileOrigem = new RandomAccessFile(fOrigem, "r");
         FileChannel channelOrigem = fileOrigem.getChannel();

         String fDestino = ("enem_brent.db"); // referencia o arquivo organizado pelo método implementado
         OrganizadorBrent org = new OrganizadorBrent(fDestino);

         // Ler cada aluno do arquivo de origem e inserir no de destino
         for (int i=0; i<9276328; i++)  {
            // Ler da origem
            ByteBuffer buff = ByteBuffer.allocate(200);
            channelOrigem.read(buff);
            buff.flip();
            Aluno a = Conversor.getAluno(buff);
            // Inserir no destino
            org.addAluno(a);
         }
         channelOrigem.close();
         fileOrigem.close();
    }
}