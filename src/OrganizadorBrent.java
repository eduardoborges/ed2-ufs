/**
 * @author  Eduardo Borges, Ismael Siveira e Pablo Lima
 * @about   Estudante de Sistemas de Informação - UFS
 * @website http://github.com/eduardoborges
 * @version 0.1b
 * @copyright MIT
 */

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

class OrganizadorBrent implements IFileOrganizer {

    // TAManho da tabela de registros do arquivo
    private final int P = 1200; 
    
    // Numero de bytes que um registro ocupa
    private final int TAM = 200;
    
    // Canal de comunicacao com o arquivo
    private FileChannel canal;
    
    public OrganizadorBrent(String path) throws FileNotFoundException{
        File file = new File(path);
        RandomAccessFile rf = new RandomAccessFile(file, "rw");
        this.canal = rf.getChannel();
    }
    
    private int calculaHash(long matricula) {
        return (int)(matricula % this.P);
    }
    
    private int calculaIncremento(long matricula) {
        return (int)(matricula % (this.P - 2)) + 1;
    }
    
    @Override
    public void addAluno(Aluno a) {
        ByteBuffer buf = ByteBuffer.allocate(this.TAM);
        
        long matric     = a.getMatric();
        int hash        = calculaHash(matric);
        int posicao     = hash * this.TAM;
        int posicao2    = posicao;
        int posicao3    = posicao;
        long x, z;

        try {
            canal.position(posicao);
            canal.read(buf);
            buf.flip();
            x = buf.getLong();
            z = x;
            buf.clear();
            
            // Se a posicao estiver livre
            if (x == 0 || x == -1) {
                canal.position(posicao);
                canal.write(Conversor.getBuffer(a));
                buf.clear();
            }

            // Houve uma colisao
            else {
                //SIMULANDO OS PASSOS PARA ADD O NOVO REGISTRO SEM MOVER O PRIMEIRO
                int colisao = (int)(x);
                int incremento = calculaIncremento(matric);
                int passos = 1;
                while(x != 0 && x != -1) {
                    passos++;
                    posicao = (((posicao/this.TAM) + incremento) % this.P) * this.TAM;
                    canal.position(posicao);
                    canal.read(buf);
                    buf.flip();
                    x = buf.getInt();
                    buf.clear();
                }
                
                //SIMULANDO OS PASSOS PARA ADD O NOVO REGISTRO MOVENDO O PRIMEIRO
                int incremento2 = calculaIncremento(colisao);
                int passos2 = 1;
                while(z != 0 && z != -1) {
                    passos2++;
                    posicao2 = (((posicao2/this.TAM) + incremento2) % this.P) * this.TAM;
                    canal.position(posicao2);
                    canal.read(buf);
                    buf.flip();
                    z = buf.getInt();
                    buf.clear();
                }
                
                // Opcao I: apenas escrever na posicao encontrada
                if ( (custoBusca(colisao)+passos) <= (custoBusca(colisao)+passos2) ) { //+1 ja incluso
                    canal.position(posicao);
                    canal.write(Conversor.getBuffer(a));
                    buf.clear();
                }
                // Opcao II: por o novo no inicio e mover o original
                else {
                    canal.position(posicao3);
                    canal.read(buf);
                    buf.clear();
                    canal.position(posicao2);
                    canal.write(buf);
                    buf.clear();
                    canal.position(posicao3);
                    canal.write(Conversor.getBuffer(a));
                    buf.clear();
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(OrganizadorSequencial.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Aluno adicionado!");
    }

    @Override
    public Aluno getAluno(long matric) {
        ByteBuffer buf = ByteBuffer.allocate(this.TAM);
        
        int hash = calculaHash(matric);
        int posicao = (int)(hash * this.TAM);
        long x, z;

        try {
            canal.position(posicao);
            canal.read(buf);
            buf.flip();
            x = buf.getLong();
            buf.clear();
            if (x == matric) {
                Aluno a = Conversor.getAluno(buf);
                return a;
            }
            else {
                int incremento = calculaIncremento(matric);
                while(x != 0) {
                    posicao = (((posicao/this.TAM) + incremento) % this.P) * this.TAM;
                    canal.position(posicao);
                    canal.read(buf);
                    buf.flip();
                    x = buf.getLong();
                    buf.clear();
                    if (x == matric) {
                        Aluno a = Conversor.getAluno(buf);
                        return a;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public Aluno delAluno(long matric) {
        ByteBuffer buf = ByteBuffer.allocate(this.TAM);
        // Informacoes do aluno removido
        Aluno removido = null;
        int hash = calculaHash(matric);
        int posicao = hash * this.TAM;
        
        try {
            canal.position(posicao);
            canal.read(buf);
            buf.flip();
            int x = buf.getInt();
            buf.clear();
            if (x == matric) {
                removido = Conversor.getAluno(buf);
                canal.position(posicao);
                canal.write(removedByteBuffer());
                return removido;
            }
            else {
                int incremento = calculaIncremento(matric);
                while(x != 0) {
                    posicao = (((posicao/this.TAM) + incremento) % this.P) * this.TAM;
                    canal.position(posicao);
                    canal.read(buf);
                    buf.flip();
                    x = buf.getInt();
                    buf.clear();
                    if (x == matric) {
                        removido = Conversor.getAluno(buf);
                        canal.position(posicao);
                        canal.write(removedByteBuffer());
                        return removido;
                    }
                }
            }
            System.out.println("Aluno inexistente!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return removido;
    }
    
    // helpers functions

    private ByteBuffer removedByteBuffer() {
        ByteBuffer b = ByteBuffer.allocate(this.TAM);
        b.putInt(-1);
        b.put("".getBytes());
        b.put("".getBytes());
        b.putShort((short) 0);
        b.put("".getBytes());
        b.put("".getBytes());
        b.flip();
        return b;
    }
    
    private int custoBusca(int matricula) {
        ByteBuffer buf = ByteBuffer.allocate(this.TAM);
        
        int hash = calculaHash(matricula);
        int posicao = hash * this.TAM;
        int passos = 1;
        
        try {
            canal.position(posicao);
            canal.read(buf);
            buf.flip();
            long x = buf.getLong();
            buf.clear();
            
            if (x == matricula) return passos;
            else {
                int incremento = calculaIncremento(matricula);
                while(x != 0) {
                    passos++;
                    posicao = (((posicao/this.TAM) + incremento) % this.P) * this.TAM;
                    canal.position(posicao);
                    canal.read(buf);
                    buf.flip();
                    x = buf.getLong();
                    buf.clear();
                    if (x == matricula) break; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passos;
    }
    
    public void inicializaArquivo(Aluno vazio) {        
        try {
            canal.position(0);
            for (int i = 0; i < this.P; i++)
                canal.write(Conversor.getBuffer(vazio));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void listarArquivo() {
        ByteBuffer buf = ByteBuffer.allocate(this.TAM);
        try {
            canal.position(0);
            for (int i = 0; i < this.P; i++) {
                canal.read(buf);
                buf.flip();
                int x = buf.getInt();
                System.out.println("[ " + i + " ] " + x);
                buf.clear();
            }
        } catch (IOException ex) {
            Logger.getLogger(OrganizadorSequencial.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}
