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
    
    private final int SIZE = 11;

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
        return (int)( (matricula / this.SIZE) % (this.SIZE));
    }
    
    /**
	 * Adiciona um Aluno no arquivo
	 * 
	 * @param alunoToInsert		Objeto com o aluno a ser inserido
	 * @return void
	 **/
    @Override
    public void addAluno(Aluno a) {
        
        long matric       = a.getMatric();
        int position      = calcHash(matric);

        // preciso de algumas copias da posicao original
        int position2 = position;
        int originalPosition = position;

        Aluno quemTaAqui = getItemIndex(position);
        int x = (int)quemTaAqui.getMatric();
        int z = x;
            
        // Se a posicao estiver livre DEUS AJUDE QUE SIM AMÉM?
        if (x == 0 || x == -1) {
            // System.out.println("entrou no if, adicionou na pos "+ hashPosition);
            this.writeAluno(a, position);
        }

        // Houve uma colisao
        else
        {
            // add sem mover o primeiro
            int colisao = x;
            Aluno contandoAluno;

            int incremento = calcIncrement(matric);            
            int passos = 1;
            while(x != 0 && x != -1) {
                passos++;
                position = (position + incremento) % this.SIZE;
                contandoAluno = this.getItemIndex(position);
                x = (int)contandoAluno.getMatric();
            }
            
            // add movendo o primeiro
            int incremento2 = calcIncrement(colisao);
            int passos2 = 1;
            while(z != 0 && z != -1) {
                passos2++;
                position2 = (position2 + incremento2) % this.SIZE;                
                contandoAluno = this.getItemIndex(position2);
                z = (int)contandoAluno.getMatric();
            }
            
            // AQUI TEMOS DUAS OPCOES
            // 1 - apenas escrever
            // 2 - colocar em outro no inicio e mover o que está lá


            // Esta se calculando apenas a colisao do primeiro item a ser inserido  
            // e necessario se calcular o incremento do item que esta armazenado la dentro do vetor

            // 1
            if ( (searchCost(colisao)+passos) <= (searchCost(colisao)+passos2) ) {
                this.writeAluno(a, position);
            }
            // 2
            else {

                // System.out.println("entrou na condicao 2");
                // System.out.println("posicao original: " + originalPosition);
                // System.out.println("posicao original: " + position2);

                Aluno tmpAluno = this.getItemIndex(originalPosition);

                this.writeAluno(tmpAluno, position2);
                this.writeAluno(a, originalPosition);



            }
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
                while(x != 0) {
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
                Aluno blankAluno = new Aluno(0, "[VAZIO]", "[VAZIO]", "[VAZIO]", (short) 0);
                canal.write(Conversor.getBuffer(blankAluno), i * Aluno.TAM);
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
		System.out.println("\n\n= ESTADO DA BASE DE DADOS ==\n");
		try {
            this.canal.position(0);            
            
            for (int i = 0; i < this.SIZE; i++){
                
                Aluno a = this.getItemIndex(i);
                String output = "-----------------------------------------\n";
                output += "| "+i+" | ";
				output += a.getMatric() + " | ";
				output += a.getNome();
                System.out.println(output);
                this.canal.position(0);
            }
            System.out.println("-----------------------------------------");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
	 * Grava um aluno no banco
	 * 
	 * @param 	aluno 				Objeto do tipo Aluno a ser inserido
	 * @return	positionToInsert 	Posicao do banco que será inserido o registro
	 */
	private void writeAluno(Aluno aluno, long positionToInsert) {
		try {
            long positionToInsertInBytes = 0;
            if(positionToInsert != 0 ){
                positionToInsertInBytes = (positionToInsert  * Aluno.TAM);
            }
			
			ByteBuffer buf = Conversor.getBuffer(aluno);
			this.canal.write(buf, positionToInsertInBytes);
            this.canal.position(0);            
		} catch (Exception e) {
			System.out.println("Erro ao gravar aluno: "+ e.getMessage());
		}
	}
	
	/**
	 * Retorna o item no banco de dados na posicao passada.
	 * 
	 * @param index 	Posição do item desejado
	 * @return 			Retorna o objeto do aluno buscado
	 **/
	private Aluno getItemIndex(int index){
		try {
            long posicaoInBytes = 0;
            if (index != 0){
			    posicaoInBytes = index * Aluno.TAM;
            }
			this.canal.position(posicaoInBytes);
			ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
			this.canal.read(buf);
            this.canal.position(0);
            buf.clear();            
			return Conversor.getAluno(buf);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
