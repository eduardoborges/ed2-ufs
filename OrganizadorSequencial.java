
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

	@Overwride
	public void addAluno(Aluno a){

	}

	@Overwride
	public void delAluno(Aluno a){

	}
	
}