
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;


class OrganizadorSequencial implements IFileOrganizer {
	
	private FileChannel canal;
	
	public OrganizadorSequencial(String fileName) throws FileNotFoundException{
		File file = new File(fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		this.canal = raf.getChannel();
	}
	
	@Override
	public void addAluno(Aluno alunoToInsert) {
		try {
			long positionToInsert = this.canal.size() / Aluno.TAM;
			int itens = (int) (long) (this.canal.size() / Aluno.TAM);

			// System.out.println("> Temos " + itens + " itens");
			// System.out.println(">>> Vamo inserir " + alunoToInsert.getNome() + " com matricula " + alunoToInsert.getMatric());

			if(itens != 0){
				for (int i = 1; i <= itens; i++) {
					Aluno currAluno = this.getItemIndex(i);
					
					if (alunoToInsert.getMatric() < currAluno.getMatric()) {
						// System.out.println(">>> " + alunoToInsert.getNome() +" ("+ alunoToInsert.getMatric() +") > "+ currAluno.getNome() +" (" + currAluno.getMatric() +"): " + i);
						positionToInsert = i;
						break;
					} else {
						positionToInsert = i+1;
					}
				}

				// System.out.println("> OK, posicao de insercao é: " + positionToInsert);
				// System.out.println("> realocar voltando");
				 
				while (itens >= positionToInsert) {
					// System.out.println(">> realocando o item " + itens + " na posicao " + (itens+1) );
					this.realocateItem(itens, itens + 1);
					itens--;
				}
			}

			// finalmente gravo
			if(positionToInsert <= 0) positionToInsert = 1;
			long positionToInsertInBytes = ((positionToInsert - 1) * Aluno.TAM);
			// System.out.println("Posicao em bytes para inserir: "+ positionToInsertInBytes);
			ByteBuffer buf = Conversor.getBuffer(alunoToInsert);
			this.canal.write(buf,positionToInsertInBytes);
			// // System.out.println("> Gravando " + alunoToInsert.getNome() + " na posicao " + positionToInsert);
			// System.out.println("FIM, PROXIMO ITEM");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

	@Override
	public Aluno delAluno(long matric) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Retorna a posicao do aluno pela matricula.
	 * 
	 * @param matric 	Matricula do aluno buscado
	 * @return			Retorna um inteiro com a posição daquele aluno
	 */
	public int getPosition(long matric){
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
	 */
	public Aluno getItemIndex(int index){
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

	public void realocateItem(int source, int target){
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
	 * Printa o estado atual dos dados
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