
/**
 * @author  Eduardo Borges
 * @about   Estudante de Sistemas de Informação - UFS
 * @website http://github.com/eduardoborges
 * @version 1.0
 */


import java.awt.print.Paper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.nio.ByteBuffer;


class OrganizadorSequencial implements IFileOrganizer {
	
	private FileChannel canal;
	
	public OrganizadorSequencial(String fileName) throws FileNotFoundException{
		File file = new File(fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		this.canal = raf.getChannel();
	}
	
	/**
	 * Adiciona um Aluno no arquivo
	 * 
	 * @param alunoToInsert		Objeto com o aluno a ser inserido
	 * @return void
	 **/
	@Override
	public void addAluno(Aluno alunoToInsert) {
		try {
			long positionToInsert 	= this.canal.size() / Aluno.TAM;
			int itens 				= (int) (long) (this.canal.size() / Aluno.TAM);

			if(itens != 0){
				for (int i = 1; i <= itens; i++) {
					Aluno currAluno = this.getItemIndex(i);
					
					if (alunoToInsert.getMatric() < currAluno.getMatric() ) {
						positionToInsert = i;
						break;
					} 
					else {
						positionToInsert = i + 1;
					}
				}

				int tamanho = itens;
				while (positionToInsert <= tamanho) {
					this.realocateItem(tamanho, tamanho+1);
					tamanho--;
				}
			} else {
				positionToInsert = 1;
			}

			this.writeAluno(alunoToInsert, positionToInsert);

			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Busca um aluno do arquivo passando sua matricula
	 * 
	 * @param 	matric	Matricula do aluno a ser buscado
	 * @return 	aluno	Um objeto Aluno com o aluno encontrado, caso contrario retorna null
	 **/
	@Override
	public Aluno getAluno(long matric) {
		try {
			this.canal.position(0);
			long size = this.canal.size();
			while( canal.position() < size){
				ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
				this.canal.read(buf);
				if(matric == buf.getLong(0)){
					buf.position(0);
					return Conversor.getAluno(buf);
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
		int realocPoint = 0;
		try {
			long size = this.canal.size();
			int itens = (int) size / Aluno.TAM;
			Aluno blankAluno = new Aluno(-1, "[VAZIO]", "[VAZIO]", "[VAZIO]", (short) 0);

			for (int i = 1; i <= itens; i++) {
				Aluno currAluno = this.getItemIndex(i);
				if (matric == currAluno.getMatric()) {
					this.writeAluno(blankAluno, i);
					realocPoint = i;
					// break;
				}
			}
			
			while (realocPoint <= itens) {
				this.realocateItem(realocPoint+1, realocPoint);
				realocPoint++;
			}

			long truncateOn = size - Aluno.TAM;
			this.canal.truncate(truncateOn);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  null;
	}
	
	/**
	 * Grava um aluno no banco
	 * 
	 * @param 	aluno 				Objeto do tipo Aluno a ser inserido
	 * @return	positionToInsert 	Posicao do banco que será inserido o registro
	 */
	private void writeAluno(Aluno aluno, long positionToInsert) {
		try {
			long positionToInsertInBytes = ((positionToInsert - 1) * Aluno.TAM);
			ByteBuffer buf = Conversor.getBuffer(aluno);
			this.canal.write(buf, positionToInsertInBytes);
		} catch (Exception e) {
			System.out.println("Erro ao gravar aluno: "+ e.getMessage());
		}
	}

	/**
	 * Retorna a posicao do aluno de uma determinada matricula
	 * 
	 * @param 	matric 	Matricula do aluno
	 * @return 	Aluno	Retorna o objeto Aluno
	 **/
	private int getPosition(long matric){
		int i = 0;
		try {
			this.canal.position(0);
			long size = this.canal.size();
			while( canal.position() < size){
				i++;
				ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
				this.canal.read(buf);
				if(matric == buf.getLong(0)){
					buf.position(0);
					return i;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i;
	}
	
	/**
	 * Retorna o item no banco de dados na posicao passada.
	 * 
	 * @param index 	Posição do item desejado
	 * @return 			Retorna o objeto do aluno buscado
	 **/
	private Aluno getItemIndex(int index){
		try {
			long posicaoBytes = (index-1) * Aluno.TAM;
			this.canal.position(posicaoBytes);
			ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
			this.canal.read(buf);
			return Conversor.getAluno(buf);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Realoca um item Aluno para outra posicao do arquivo
	 * 
	 * @param source Posição do dado origem
	 * @param target Posição destino
	 * @return void
	*/
	private void realocateItem(int source, int target){
		try {
			Aluno alunoSource = this.getItemIndex(source);
			ByteBuffer buf = Conversor.getBuffer(alunoSource);
			int positionToInsert = (target-1) * Aluno.TAM;
			this.canal.write(buf, positionToInsert);
			
		} catch (Exception e) {
			System.out.println("Ocorreu um erro ao realocar Aluno: " + e.getMessage());
		}
	}


	/**
	 * Printa o estado atual dos dados do arquivo
	 */
	public void showAllData() {
		System.out.println("\n\n===== Estado dos Dados =======\n");
		String output = "";
		try {
			this.canal.position(0);
			long size = this.canal.size();
			while( canal.position() < size){
				ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
				this.canal.read(buf);
				Aluno a = Conversor.getAluno(buf);
				output = output + a.getMatric() + " - ";
				output = output + a.getNome() + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(output);
	}
}