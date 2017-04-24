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

class OrganizadorBrent implements IFileOrganizer {
    
    private final int SIZE = 25;

    // Canal de comunicacao com o arquivo
    private FileChannel canal;
    
    public OrganizadorBrent(String fileName) throws FileNotFoundException{

        File file = new File(fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		this.canal = raf.getChannel();
        
    }
    /**
	 * Adiciona um Aluno no arquivo
	 * 
	 * @param matricula		Matricula do Aluno a ser processado
	 * @return hash         Posicao hash a ser inserido
	 **/
    private int calcHash(long matricula) {
        return (int)(matricula % this.SIZE);
    }
    
    /**
	 * Calcula o incremento do registro Aluno
	 * 
	 * @param matricula		Matricula do Aluno a ser processado
	 * @return increment    O incremento da posicao do aluno
     **/
    private int calcIncrement(long matricula) {
        return (int)(matricula % (this.SIZE - 2)) + 1;
    }
    
    /**
	 * Adiciona um Aluno no arquivo
	 * 
	 * @param alunoToInsert		Objeto com o aluno a ser inserido
	 * @return void
	 **/
    @Override
    public void addAluno(Aluno a) {
        ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
        
        long matric     = a.getMatric();
        int hash        = calcHash(matric);
        int posicao     = hash * Aluno.TAM;
        int posicao2    = posicao;
        int posicao3    = posicao;
        int x, z;

        try {
            canal.position(posicao);
            canal.read(buf);
            buf.flip();
            x = (int)buf.getLong();
            z = x;
            buf.clear();
            
            // Se a posicao estiver livre DEUS AJUDE QUE SIM AMÉM?
            if (x == 0 || x == -1) {
                canal.position(posicao);
                canal.write(Conversor.getBuffer(a));
                buf.clear();
            }

            // Houve uma colisao
            else {
                // add sem mover o primeiro
                int colisao = x;
                int incremento = calcIncrement(matric);
                int passos = 1;
                while(x != 0 && x != -1) {
                    passos++;
                    posicao = (((posicao/Aluno.TAM) + incremento) % this.SIZE) * Aluno.TAM;
                    canal.position(posicao);
                    canal.read(buf);
                    buf.flip();
                    x = (int)(buf.getLong());
                    buf.clear();
                }
                
                // add movendo o primeiro
                int incremento2 = calcIncrement(colisao);
                int passos2 = 1;
                while(z != 0 && z != -1) {
                    passos2++;
                    posicao2 = (((posicao2/Aluno.TAM) + incremento2) % this.SIZE) * Aluno.TAM;
                    canal.position(posicao2);
                    canal.read(buf);
                    buf.flip();
                    z = (int)(buf.getLong());
                    buf.clear();
                }
                
                // AQUI TEMOS DUAS OPCOES
                // 1 - apenas escrever ou
                // 2 - colocar em outro no inicio e mover o que está lá


                // 1
                if ( (searchCost(colisao)+passos) <= (searchCost(colisao)+passos2) ) {
                    canal.position(posicao);
                    canal.write(Conversor.getBuffer(a));
                    buf.clear();
                }
                // 2
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
            System.out.println(ex.getMessage());
        }
        System.out.println("Aluno adicionado!");
    }

    /**
	 * Busca um aluno do arquivo passando sua matricula
	 * 
	 * @param 	matric	Matricula do aluno a ser buscado
	 * @return 	aluno	Um objeto Aluno com o aluno encontrado, caso contrario retorna null
	 **/
    @Override
    public Aluno getAluno(long matric) {
        ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
        
        int hash = this.calcHash(matric);
        int posicao = (hash * Aluno.TAM);

        try {
            canal.position(posicao);
            canal.read(buf);
            buf.flip();
            int x = (int)buf.getLong();
            buf.clear();
            if (x == matric) {
                Aluno a = Conversor.getAluno(buf);
                return a;
            }
            else {
                int incremento = this.calcIncrement(matric);
                while(x != 0 || x != -1) {
                    posicao = (((posicao/Aluno.TAM) + incremento) % this.SIZE) * Aluno.TAM;
                    canal.position(posicao);
                    canal.read(buf);
                    buf.flip();
                    x = (int)buf.getLong();
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
    /**
	 * Remove um aluno do arquivo passando sua matricula
	 * 
	 * @param 	matric	Matricula do aluno a ser removido
	 * @return 	aluno	Um objeto com o al	uno adicionado
	 **/
    @Override
    public Aluno delAluno(long matric) {
        ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
        // Informacoes do aluno removido
        Aluno removido = null;
        int hash = this.calcHash(matric);
        int posicao = hash * Aluno.TAM;
        Aluno remAluno = new Aluno(-1, "[REMOVIDO]", "[REMOVIDO]", "[REMOVIDO]", (short) 0);
        
        try {
            canal.position(posicao);
            canal.read(buf);
            buf.flip();
            int x = (int)buf.getLong();
            buf.clear();
            if (x == matric) {
                removido = Conversor.getAluno(buf);
                canal.position(posicao);
                canal.write(Conversor.getBuffer(remAluno));
                return removido;
            }
            else {
                int incremento = calcIncrement(matric);
                while(x != 0) {
                    posicao = (((posicao/Aluno.TAM) + incremento) % this.SIZE) * Aluno.TAM;
                    canal.position(posicao);
                    canal.read(buf);
                    buf.flip();
                    x = (int)buf.getLong();
                    buf.clear();
                    if (x == matric) {
                        removido = Conversor.getAluno(buf);
                        canal.position(posicao);
                        canal.write(Conversor.getBuffer(remAluno));
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


    /**
     * Inicializa a base com campos vazios.
     * Isso é necessário porque vc vai acessor campos
     * e eles podem não existir fisicamente, então
     * cria logo os bagui tudo vazio e vai que é tua.
     * 
     * @return void
     */
    public void initDatabase() {        
        try {
            canal.position(0);
            for (int i = 0; i < this.SIZE; i++){
                Aluno blankAluno = new Aluno(-1, "[VAZIO]", "[VAZIO]", "[VAZIO]", (short) 0);
                canal.write(Conversor.getBuffer(blankAluno));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * Calcula o custo da busca de um Aluno na base
     * 
     * @param matricula     Matricula do aluno a ser processado
     * @return passos       Um inteiro com a quantidade de passos para executar a instruçao.
     */
    private int searchCost(int matricula) {
        ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
        
        int hash    = calcHash(matricula);
        int posicao = hash * Aluno.TAM;
        int passos  = 1;
        
        try {
            canal.position(posicao);
            canal.read(buf);
            buf.flip();
            int x = (int)(buf.getLong());
            buf.clear();
            
            if (x == matricula) return passos;
            else {
                int incremento = this.calcIncrement(matricula);
                while(x != 0) {
                    passos++;
                    posicao = (((posicao/Aluno.TAM) + incremento) % this.SIZE) * Aluno.TAM;
                    canal.position(posicao);
                    canal.read(buf);
                    buf.flip();
                    x = (int)buf.getLong();
                    buf.clear();
                    if (x == matricula) break; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passos;
    }

    /**
	 * Printa o estado atual dos dados do arquivo
     * 
     * @return void
	 */
    public void showAllData() {
		System.out.println("\n\n= ESTADO DA BASE DE DADOS =======\n");
		try {
            for (int i = 0; i < this.SIZE; i++){
                
                ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
				this.canal.read(buf);
				Aluno a = Conversor.getAluno(buf);
                String output = "";
                output += "| "+i+" |";
				output += a.getMatric() + " | ";
				output += a.getNome();
                System.out.println(output);

            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
